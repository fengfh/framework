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
package leap.web.route;

import java.util.function.Function;
import java.util.function.Supplier;

import leap.lang.Assert;
import leap.lang.http.HTTP.Method;
import leap.web.Handler;
import leap.web.action.Action;
import leap.web.action.HandlerAction;
import leap.web.action.RunnableAction;

class RouteConfiguratorImpl implements RouteConfigurator {
	
	private final Function<RouteConfiguratorImpl, Route> callback;
	private boolean apply;
	
	protected Method  method;
	protected String  path;
	protected Action  handler;
	protected boolean supportsMulitpart;
	protected Boolean corsEnabled;
	protected Boolean csrfEnabled;
	protected Boolean httpsOnly;
	protected Boolean allowAnonymous;
	protected Boolean allowClientOnly;
	
	public RouteConfiguratorImpl(Function<RouteConfiguratorImpl, Route> callback) {
		this.callback = callback;
	}
	
	public Method getMethod() {
		return method;
	}

	public String getPath() {
		return path;
	}

	public Action getHandler() {
		return handler;
	}

	public boolean isSupportsMulitpart() {
		return supportsMulitpart;
	}

	public Boolean getCorsEnabled() {
		return corsEnabled;
	}
	
	public Boolean getCsrfEnabled() {
		return csrfEnabled;
	}
	
	public Boolean getHttpsOnly() {
	    return httpsOnly;
	}
	
	public Boolean getAllowAnonymous() {
	    return allowAnonymous;
	}
	
	public Boolean getAllowClientOnly() {
	    return allowClientOnly;
	}
	
	@Override
    public RouteConfigurator handle(Method method, String path, Handler handler) {
	    return handle(method, path, () -> new HandlerAction(handler));
    }

	@Override
    public RouteConfigurator handle(Method method, String path, Runnable handler) {
		return handle(method, path, () -> new RunnableAction(handler));
    }
	
	protected RouteConfigurator handle(Method method, String path, Supplier<Action> handler) {
		this.method  = method;
		this.path    = path;
		this.handler = handler.get();
		return this;
	}

	@Override
	public RouteConfigurator setSupportsMultipart(boolean v) {
		this.supportsMulitpart = v;
		return this;
	}

	@Override
	public RouteConfigurator setCorsEnabled(boolean enabled) {
		this.corsEnabled = enabled;
		return this;
	}
	
	@Override
    public RouteConfigurator setCsrfEnabled(boolean enabled) {
		this.csrfEnabled = enabled;
	    return this;
    }
	
	@Override
    public RouteConfigurator setHttpsOnly(boolean httpsOnly) {
	    this.httpsOnly = httpsOnly;
        return this;
    }
	
    @Override
    public RouteConfigurator setAllowAnonymous(boolean allow) {
        this.allowAnonymous = allow;
        return this;
    }

    @Override
    public RouteConfigurator setAllowClientOnly(boolean allow) {
        this.allowClientOnly = allow;
        return this;
    }

    @Override
	public Route apply() {
		Assert.isFalse(apply, "The route aleady added");
		
		Assert.notEmpty(path,   "'path' cannot be empty");
		Assert.notNull(handler, "'handler' cannot be null");
		
		apply = true;
		return callback.apply(this);
	}

}