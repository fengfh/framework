/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package leap.web.api.mvc;

import leap.lang.http.HTTP;
import leap.web.Response;

public interface ApiErrorHandler {

    default void unauthorized(Response response) {
        unauthorized(response, "Unauthorized");
    }

    default void unauthorized(Response response, String message) {
        responseError(response, HTTP.SC_UNAUTHORIZED,  HTTP.Status.UNAUTHORIZED.name(), message);
    }

    default void forbidden(Response response) {
        forbidden(response, "Forbidden");
    }

    default void forbidden(Response response, String message) {
        responseError(response, HTTP.SC_FORBIDDEN, HTTP.Status.FORBIDDEN.name(), message);
    }

    default void notFound(Response response) {
        notFound(response, "Not found");
    }

    default void notFound(Response response, String message) {
        responseError(response, HTTP.SC_NOT_FOUND, HTTP.Status.NOT_FOUND.name(), message);
    }

    default void badRequest(Response response) {
        badRequest(response, "Bad request");
    }

    default void badRequest(Response response, String message) {
        responseError(response, HTTP.SC_BAD_REQUEST, HTTP.Status.BAD_REQUEST.name(), message);
    }

    default void internalServerError(Response response, String message) {
        responseError(response, HTTP.SC_INTERNAL_SERVER_ERROR, HTTP.Status.INTERNAL_SERVER_ERROR.name(), message);
    }

    default void internalServerError(Response response, Throwable cause) {
        responseError(response, HTTP.SC_INTERNAL_SERVER_ERROR, HTTP.Status.INTERNAL_SERVER_ERROR.name(), cause.getMessage());
    }

    default void responseError(Response response, int status, String message) {
        responseError(response, status, new ApiError(status, null, message));
    }

    default void responseError(Response response, int status, String code, String message) {
        responseError(response, status, new ApiError(status, code, message));
    }

    void responseError(Response response, int status, ApiError error);

}
