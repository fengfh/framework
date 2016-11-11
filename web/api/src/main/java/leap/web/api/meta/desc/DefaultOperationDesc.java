/*
 *
 *  * Copyright 2013 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package leap.web.api.meta.desc;

import leap.web.action.Action;
import leap.web.action.Argument;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by kael on 2016/11/8.
 */
public class DefaultOperationDesc implements OperationDesc {

    private String summary;
    private String description;
    private Action action;

    private Map<String, ParameterDesc> params = new ConcurrentHashMap<>();

    @Override
    public String getSummary() {
        return summary;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Action getAction() {
        return action;
    }

    @Override
    public ParameterDesc getParameter(Argument argument) {
        return params.get(argument.getName());
    }

    public void addParameter(ParameterDesc param){
        params.put(param.getArgument().getName(),param);
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAction(Action action) {
        this.action = action;
    }

}