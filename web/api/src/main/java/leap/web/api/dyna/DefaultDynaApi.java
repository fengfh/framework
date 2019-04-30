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

package leap.web.api.dyna;

import leap.web.api.Api;
import leap.web.api.config.ApiConfig;
import leap.web.api.config.ApiConfigurator;
import leap.web.api.meta.ApiMetadata;

import java.util.Map;

public class DefaultDynaApi implements DynaApi {

    private final Api api;

    public DefaultDynaApi(Api api) {
        this.api = api;
    }

    @Override
    public Map<Class<?>, Object> getExtensions() {
        return api.getExtensions();
    }

    @Override
    public <T> void setExtension(Class<T> type, Object extension) {
        api.setExtension(type, extension);
    }

    @Override
    public <T> T removeExtension(Class<?> type) {
        return api.removeExtension(type);
    }

    @Override
    public <T> T getExtension(Class<?> type) {
        return api.getExtension(type);
    }

    @Override
    public String getName() {
        return api.getName();
    }

    @Override
    public String getBasePath() {
        return api.getBasePath();
    }

    @Override
    public ApiConfig getConfig() {
        return api.getConfig();
    }

    @Override
    public ApiConfigurator getConfigurator() throws IllegalStateException {
        return api.getConfigurator();
    }

    @Override
    public ApiMetadata getMetadata() throws IllegalStateException {
        return api.getMetadata();
    }

    @Override
    public void setMetadata(ApiMetadata metadata) throws IllegalStateException {
        api.setMetadata(metadata);
    }

    @Override
    public boolean isCreated() {
        return api.isCreated();
    }

    @Override
    public void markCreated() throws IllegalStateException {
        api.markCreated();
    }

    @Override
    public void create() throws IllegalStateException {
        api.create();
    }
}