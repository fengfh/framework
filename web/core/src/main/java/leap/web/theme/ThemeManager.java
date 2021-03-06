/*
 * Copyright 2014 the original author or authors.
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
package leap.web.theme;

import leap.lang.exception.ObjectNotFoundException;
import leap.web.Request;
import leap.web.view.View;

public interface ThemeManager {

	/**
	 * Returns the {@link Theme} which match the given name.
	 * 
	 * @throws ObjectNotFoundException if no theme match the given name.
	 */
	Theme getTheme(String name) throws ObjectNotFoundException;
	
	/**
	 * Returns the {@link Theme} which match the given name or <code>null</code> if no theme matched.
	 */
	Theme tryGetTheme(String name);
	
	/**
	 * Resolving current theme from http request.
	 */
	Theme resolveTheme(Request request) throws Throwable;

    /**
     * Returns the view in default theme.
     *
     * <p/>
     * If no default theme exists, returns the view from default {@link leap.web.view.ViewSource}.
     */
    View getDefaultView(String viewName);

    /**
     * Returns the default theme or null if no default theme exists.
     */
	Theme getDefaultTheme();
	
}