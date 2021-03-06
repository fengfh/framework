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

package leap.web.api.restd.crud;

import leap.lang.Strings;
import leap.orm.dao.Dao;
import leap.web.Request;
import leap.web.action.ActionParams;
import leap.web.action.FuncActionBuilder;
import leap.web.api.Api;
import leap.web.api.config.ApiConfigurator;
import leap.web.api.meta.model.MApiModel;
import leap.web.api.mvc.ApiResponse;
import leap.web.api.mvc.params.QueryOptions;
import leap.web.api.orm.*;
import leap.web.api.restd.CrudOperation;
import leap.web.api.restd.CrudOperationBase;
import leap.web.api.restd.RestdContext;
import leap.web.api.restd.RestdModel;
import leap.web.route.RouteBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Query records operation.
 */
public class QueryOperation extends CrudOperationBase implements CrudOperation {

    protected static final String NAME = "query";

    public static final String QUERY_EXPAND_ERROR = "webapi.errors.query_expand_error";
    public static final String ALLOW_EXPAND_ERROR = "x-allow-expand-err";
    public static final String ERROR_EXPANDS      = "x-err-expands";

    @Override
    public void createCrudOperation(ApiConfigurator c, RestdContext context, RestdModel model) {
        if (!context.getConfig().allowQueryModel(model.getName())) {
            return;
        }

        String path = fullModelPath(c, model);
        String name = Strings.lowerCamel(NAME, model.getName());

        createCrudOperation(c, context, model, path, name, null);
    }

    public void createCrudOperation(ApiConfigurator c, RestdContext context, RestdModel model,
                                    String path, String name, Callback callback) {
        FuncActionBuilder action = new FuncActionBuilder(name);
        RouteBuilder      route  = rm.createRoute("GET", path);

        if (null != callback) {
            callback.preAddArguments(action);
        }

        action.setFunction(createFunction(context, model, action.getArguments().size()));

        addArgument(context, action, QueryOptions.class, "options");

        if (null != callback) {
            callback.postAddArguments(action);
        }

        addModelQueryResponse(action, model);

        preConfigure(context, model, action);
        route.setAction(action.build());
        setCrudOperation(route, NAME);
        postConfigure(context, model, route);

        if (isOperationExists(context, route)) {
            return;
        }

        c.addDynamicRoute(rm.loadRoute(context.getRoutes(), route));
    }

    protected Function<ActionParams, Object> createFunction(RestdContext context, RestdModel model, int start) {
        return new QueryFunction(context.getApi(), context.getDao(), model, start);
    }

    protected class QueryFunction extends CrudFunction implements Function<ActionParams, Object> {

        public QueryFunction(Api api, Dao dao, RestdModel model, int start) {
            super(api, dao, model, start);
        }

        @Override
        public Object apply(ActionParams params) {
            MApiModel am = am();

            final Request request = params.getContext().getRequest();

            ModelExecutorContext context  = new SimpleModelExecutorContext(api, dao, am, em, params);
            ModelQueryExecutor   executor = newQueryExecutor(context);

            QueryOptions options = getWithoutId(params, 0);

            QueryListResult result = executor.queryList(options);

            boolean hasExpandErrors = null != result.getExpandErrors() && !result.getExpandErrors().isEmpty();
            if (hasExpandErrors && !allowExpandErrors(request)) {
                return ApiResponse.err(request.getMessageSource().getMessage(QUERY_EXPAND_ERROR));
            }

            ApiResponse response;
            if (null != result.getEntity()) {
                response = ApiResponse.of(result.getEntity());
            } else if (result.getCount() == -1) {
                response = ApiResponse.of(result.getList());
            } else {
                response = ApiResponse.of(result.getList()).setHeader("X-Total-Count", String.valueOf(result.getCount()));
            }

            if (hasExpandErrors) {
                List<String> expands = new ArrayList<>();
                for (ExpandError ee : result.getExpandErrors()) {
                    expands.add(ee.getExpand());
                }
                response.withHeader(ERROR_EXPANDS, Strings.join(expands, ','));
            }

            return response;
        }

        protected ModelQueryExecutor newQueryExecutor(ModelExecutorContext context) {
            return mef.newQueryExecutor(context);
        }

        protected boolean allowExpandErrors(Request request) {
            return "1".equals(request.getHeader(ALLOW_EXPAND_ERROR));
        }

        @Override
        public String toString() {
            return "Function:" + "Query " + model.getName() + "";
        }
    }

}
