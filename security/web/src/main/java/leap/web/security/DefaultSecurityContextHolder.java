/*
 * Copyright 2013 the original author or authors.
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
package leap.web.security;

import leap.core.security.Credentials;
import leap.core.security.SecurityContext;
import leap.core.security.UserPrincipal;
import leap.core.validation.Validation;
import leap.lang.logging.Log;
import leap.lang.logging.LogFactory;
import leap.web.Request;
import leap.web.security.authc.Authentication;
import leap.web.security.authc.AuthenticationContext;
import leap.web.security.login.LoginContext;
import leap.web.security.logout.LogoutContext;

public class DefaultSecurityContextHolder extends SecurityContext implements SecurityContextHolder {

	private static final Log log = LogFactory.get(DefaultSecurityContextHolder.class);

	protected final SecurityConfig config;
	protected final Request        request;

    protected Authentication       authentication;
    protected LoginContext         loginContext;
    protected LogoutContext        logoutContext;
	protected String			   authenticationToken;
	
	public DefaultSecurityContextHolder(SecurityConfig config, Request request){
		this.config  = config;
		this.request = request;
	}
	
	@Override
    public Validation validation() {
	    return request.getValidation();
    }

	@Override
    public SecurityConfig getSecurityConfig() {
	    return config;
    }
	
	@Override
    public SecurityContext getSecurityContext() {
	    return this;
    }

	@Override
	public String getAuthenticationToken() {
		return authenticationToken;
	}

	@Override
	public void setAuthenticationToken(String token) {
        log.debug("Set authentication token : {}", token);
		this.authenticationToken = token;
	}

	@Override
    public Authentication getAuthentication() {
        return authentication;
    }

    public void setAuthentication(Authentication authentication) {
        log.debug("Set authentication : {}", authentication);
        this.authentication = authentication;
    }

    public LoginContext getLoginContext() {
		if(null == loginContext){
			loginContext = new DefaultLoginContext();
		}
	    return loginContext;
    }

    public LogoutContext getLogoutContext() {
		if(null == logoutContext){
			logoutContext = new DefaultLogoutContext();
		}
	    return logoutContext;
    }
	
	protected abstract class AbstractContext implements AuthenticationContext {
		@Override
        public SecurityConfig getSecurityConfig() {
	        return config;
        }

		@Override
        public SecurityContext getSecurityContext() {
	        return DefaultSecurityContextHolder.this;
        }

		@Override
        public Validation validation() {
	        return DefaultSecurityContextHolder.this.validation();
        }
	}

	protected final class DefaultLoginContext extends AbstractContext implements LoginContext {
		
        private String        returnUrl;
        private String        loginUrl;
        private boolean       error;
        private Credentials   credentials;
        private UserPrincipal user;

		@Override
		public String getAuthenticationToken() {
			return DefaultSecurityContextHolder.this.getAuthenticationToken();
		}

		@Override
		public void setAuthenticationToken(String token) {
			DefaultSecurityContextHolder.this.setAuthenticationToken(token);
		}

		@Override
        public Authentication getAuthentication() {
	        return DefaultSecurityContextHolder.this.authentication;
        }

		@Override
        public void setAuthentication(Authentication auth) {
			DefaultSecurityContextHolder.this.setAuthentication(auth);
		}

		@Override
        public String getReturnUrl() {
	        return returnUrl;
        }

		@Override
        public void setReturnUrl(String returnUrl) {
		    this.returnUrl = returnUrl;
		}

		@Override
        public String getLoginUrl() {
	        return loginUrl;
        }

		@Override
        public void setLoginUrl(String url) {
		    this.loginUrl = url;
		}
		    
		@Override
        public boolean isError() {
	        return error;
        }

		@Override
        public void setError(boolean error) {
			this.error = error;
        }

		@Override
        public boolean isCredentialsResolved() {
	        return null != credentials;
        }

		@Override
        public Credentials getCredentials() {
	        return credentials;
        }

		@Override
        public void setCredentials(Credentials credentials) {
			this.credentials = credentials;
        }

		@Override
        public boolean isAuthenticated() {
	        return null != user && user.isAuthenticated();
        }

		@Override
        public UserPrincipal getUser() {
	        return user;
        }

		@Override
        public void setUser(UserPrincipal user) {
			this.user = user;
        }
	}
	
	protected final class DefaultLogoutContext extends AbstractContext implements LogoutContext {

		private String returnUrl;

		@Override
		public String getAuthenticationToken() {
			return DefaultSecurityContextHolder.this.getAuthenticationToken();
		}

		@Override
		public void setAuthenticationToken(String token) {
			DefaultSecurityContextHolder.this.setAuthenticationToken(token);
		}

		@Override
        public Authentication getAuthentication() {
	        return DefaultSecurityContextHolder.this.authentication;
        }

		@Override
        public void setAuthentication(Authentication auth) {
			DefaultSecurityContextHolder.this.setAuthentication(auth);
		}

		public String getReturnUrl() {
			return returnUrl;
		}

		public void setReturnUrl(String returnUrl) {
			this.returnUrl = returnUrl;
		}
	}
}