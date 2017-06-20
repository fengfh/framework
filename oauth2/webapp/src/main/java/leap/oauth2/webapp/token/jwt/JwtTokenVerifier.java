/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package leap.oauth2.webapp.token.jwt;

import leap.core.AppConfigException;
import leap.core.annotation.Inject;
import leap.core.security.token.TokenVerifyException;
import leap.core.security.token.jwt.JwtVerifier;
import leap.core.security.token.jwt.RsaVerifier;
import leap.lang.Strings;
import leap.lang.http.client.HttpClient;
import leap.lang.http.client.HttpResponse;
import leap.lang.logging.Log;
import leap.lang.logging.LogFactory;
import leap.lang.security.RSA;
import leap.oauth2.webapp.OAuth2InternalServerException;
import leap.oauth2.webapp.OAuth2Config;
import leap.oauth2.webapp.token.*;
import leap.web.security.SecurityConfig;

import java.security.interfaces.RSAPublicKey;
import java.util.Map;
import java.util.Objects;

public class JwtTokenVerifier implements TokenVerifier {

    private static final Log log = LogFactory.get(JwtTokenVerifier.class);

    protected @Inject SecurityConfig sc;
    protected @Inject OAuth2Config   config;
    protected @Inject HttpClient     httpClient;

    private volatile JwtVerifier verifier;

    @Override
    public TokenInfo verifyToken(Token token) throws TokenVerifyException {
        if(null == verifier) {
            if(Strings.isEmpty(config.getPublicKeyUrl())) {
                throw new AppConfigException("publicKeyUrl must be configured");
            }
            refreshJwtVerifier();
        }

        return verify(verifier, token.getToken());
    }

    protected void refreshJwtVerifier() {
        log.info("Fetching public key from server, url '{}' ...", config.getPublicKeyUrl());
        HttpResponse response = httpClient.request(config.getPublicKeyUrl()).get();
        if(!response.isOk()) {
            throw new OAuth2InternalServerException("Error fetching public key from server, status " + response.getStatus() + "");
        }

        String       encoded   = response.getString();
        RSAPublicKey publicKey = RSA.decodePublicKey(encoded);

        verifier = new RsaVerifier(publicKey);
    }

    protected TokenInfo verify(JwtVerifier verifier, String token) throws TokenVerifyException {
        Map<String,Object> jwtDetail;

        try {
            jwtDetail = verifier.verify(token);
        }catch (TokenVerifyException e) {
            refreshJwtVerifier();
            jwtDetail = verifier.verify(token);
        }

        SimpleTokenDetails tokenInfo = new SimpleTokenDetails(token);

        String userId   = (String)jwtDetail.remove("user_id");
        String username = Objects.toString(jwtDetail.remove("username"));

        tokenInfo.setUserId(userId);
        tokenInfo.setScope((String)jwtDetail.remove("scope"));
        tokenInfo.setClientId((String)jwtDetail.remove("client_id"));

        //todo: userinfo

        //TODO How to ensure is expired?
        tokenInfo.setCreated(System.currentTimeMillis());
        try {
            Object expiresIn = jwtDetail.get("expires_in");
            if(expiresIn == null){
                //todo:
                throw new IllegalStateException("'expires_in' not found in jwt token");
            }else{
                int second = expiresIn instanceof Integer?(Integer)expiresIn:Integer.parseInt(expiresIn.toString());
                tokenInfo.setExpiresIn(second * 1000);
            }
        } catch (NumberFormatException e) {
            //todo :
            throw new IllegalStateException("Invalid expires_in : " + e.getMessage(), e);
        }

        return tokenInfo;
    }

}