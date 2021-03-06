/*
 *
 *  * Copyright 2019 the original author or authors.
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

package leap.core.bean;

import leap.core.AppConfig;
import leap.core.BeanFactory;
import leap.core.annotation.Inject;
import leap.core.config.ConfigUtils;
import leap.core.validation.BeanValidator;
import leap.core.validation.Valid;
import leap.lang.Classes;
import leap.lang.Strings;
import leap.lang.convert.Converts;
import leap.lang.json.JSON;
import leap.lang.reflect.Reflection;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DefaultBeanCreator implements BeanCreator {

    @Inject
    protected AppConfig config;

    @Inject
    protected BeanFactory factory;

    @Inject
    protected BeanAutowirer autowier;

    @Inject
    protected BeanValidator validator;

    @Override
    public <T> T tryCreateBean(Class<T> type, String configurationPrefix) {
        Map<String, Object> props = ConfigUtils.extractMap(config, configurationPrefix);
        if (props.isEmpty()) {
            return null;
        }

        String beanType = (String) props.remove("type");
        if (Strings.isEmpty(beanType)) {
            return null;
        }

        Boolean enabled = (Boolean) props.remove("enabled");

        BeanDef def = new BeanDef();
        def.setType(beanType);
        def.setEnabled(enabled);
        def.setConfig(props);

        return tryCreateBean(type, def);
    }

    @Override
    public <T> T tryCreateBean(Class<T> type, BeanDef def) {
        if (!def.isEnabled()) {
            return null;
        }

        T bean;
        if (!Strings.isEmpty(def.getClassName())) {
            Class<?> c = Classes.tryForName(def.getClassName());
            if (null == c) {
                throw new IllegalStateException("Class '" + def.getClassName() + "' not found");
            }
            if (!type.isAssignableFrom(c)) {
                throw new IllegalStateException("Class '" + def.getClassName() + "' must be sub-class of '" + type.getName() + "'");
            }
            bean = (T) Reflection.newInstance(c);
            if (null != autowier) {
                autowier.autowire(bean);
            } else {
                factory.inject(bean);
            }
        } else if (!Strings.isEmpty(def.getType())) {
            bean = factory.tryCreateBean(type, def.getType());
            if (null == bean) {
                bean = factory.tryGetBean(type, def.getType());
            }
            if (null == bean) {
                throw new IllegalStateException("Bean '" + def.getType() + "' is not exists for '" + type.getName() + "'");
            }
        } else {
            throw new IllegalStateException("Type 'type' or 'className' must be exists at bean definition");
        }

        if (bean instanceof ConfigurableBean) {
            ConfigurableBean cb = (ConfigurableBean) bean;

            Class<?> cc = cb.getConfigurationClass();
            if (null != cc && !Object.class.equals(cc)) {
                if (Map.class.equals(cc)) {
                    cb.initConfiguration(def.getConfig());
                } else if (Map.class.isAssignableFrom(cc)) {
                    Map map = (Map) Reflection.newInstance(cc);
                    map.putAll(def.getConfig());
                    cb.initConfiguration(map);
                } else {
                    Set<String> missingProperties = JSON.checkMissingProperties(cc, def.getConfig());
                    if (!missingProperties.isEmpty()) {
                        for (String p : missingProperties) {
                            if (p.equals("$") || p.endsWith(".$")) {
                                continue;
                            }
                            throw new IllegalStateException("Invalid property '" + missingProperties.iterator().next() +
                                    "' at the config of '" + type.getSimpleName() + " : " + def.getType() + "'");
                        }
                    }
                    Object config = Converts.convert(def.getConfig(), cc);
                    if (config.getClass().isAnnotationPresent(Valid.class)) {
                        validator.validate(type.getSimpleName() + "(" + def.getType() + ")", config);
                    }
                    cb.initConfiguration(config);
                }
            }
        }

        return bean;
    }

    @Override
    public <T> T[] createBeans(Class<T> type, BeanDef[] defs) {
        List<T> list = new ArrayList<>();
        if (null != defs) {
            for (BeanDef def : defs) {
                T bean = tryCreateBean(type, def);
                if (null != bean) {
                    list.add(bean);
                }
            }
        }
        return list.toArray((T[]) Array.newInstance(type, list.size()));
    }

}