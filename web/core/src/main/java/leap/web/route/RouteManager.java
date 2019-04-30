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

package leap.web.route;

import leap.lang.Strings;
import leap.web.action.Action;

public interface RouteManager {

    /**
     * Creates a new {@link Routes} object.
     */
    Routes createRoutes();

    /**
     * Creates a new {@link Routes} object.
     */
    Routes createRoutes(String pathPrefix);

    /**
     * Creates a new {@link RouteBuilder} for building a {@link Route}.
     *
     * @param method http method name, can use "*".
     */
    RouteBuilder createRoute(String method, String pathTemplate);

    /**
     * Creates a new {@link RouteBuilder} for building a {@link Route}.
     *
     * @param method http method name, can use "*".
     */
    RouteBuilder createRoute(String method, String pathTemplate, Action action);

    /**
     * Loads the {@link RouteBuilder} into the given {@link Routes}.
     */
    Route loadRoute(Routes routes, RouteBuilder route);

    /**
     * Loads all the routes defined in the controller class into the given {@link Routes}.
     */
    default Route[] loadRoutesFromController(Routes routes, Class<?> controllerClass) {
        return loadRoutesFromController(routes, controllerClass, Strings.isEmpty(routes.getPathPrefix()) ? "/" : routes.getPathPrefix());
    }

    /**
     * Loads all the routes defined in the controller class into the given {@link Routes}.
     */
    Route[] loadRoutesFromController(Routes routes, Class<?> controllerClass,String basePath);

    /**
     * Loads all the routes defined in the controller object into the given {@link Routes}.
     */
    default Route[] loadRoutesFromController(Routes routes, Object controller) {
        return loadRoutesFromController(routes, controller, Strings.isEmpty(routes.getPathPrefix()) ? "/" : routes.getPathPrefix());
    }

    /**
     * Loads all the routes defined in the controller object into the given {@link Routes}.
     */
    Route[] loadRoutesFromController(Routes routes, Object controller,String basePath);

}