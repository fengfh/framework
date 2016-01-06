/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package leap.oauth2.wac.token;

import leap.oauth2.wac.OAuth2AccessToken;
import leap.web.Request;
import leap.web.security.authc.Authentication;

/**
 * The token manager used by web app client.
 */
public interface WacTokenManager {

    /**
     * Optional. Resolves the access token from request.
     */
    default OAuth2AccessToken resolveAccessToken(Request request) {
        return resolveAccessToken(request, true);
    }

    /**
     * Optional. Resolves the access token from request and refresh it automatically if expired.
     */
    OAuth2AccessToken resolveAccessToken(Request request, boolean refreshExpired);

    OAuth2AccessToken fetchAndSaveAccessToken(Request request, Authentication authc, String code);
    
    OAuth2AccessToken refreshAndSaveAccessToken(Request request);
    
    OAuth2AccessToken refreshAndSaveAccessToken(Request request, OAuth2AccessToken old);

}