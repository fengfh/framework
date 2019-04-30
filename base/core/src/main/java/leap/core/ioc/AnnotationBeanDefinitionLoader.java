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
package leap.core.ioc;

import leap.core.annotation.Bean;
import leap.lang.Arrays2;
import leap.lang.Classes;
import leap.lang.Strings;
import leap.lang.beans.BeanType;
import leap.lang.logging.Log;
import leap.lang.logging.LogFactory;


class AnnotationBeanDefinitionLoader {
    private static final Log log = LogFactory.get(AnnotationBeanDefinitionLoader.class);

	public void load(final BeanContainer container, Class<?>[] classes) {
		if(null == container.getAppConfig()){
			return;
		}
		
		String basePackage = container.getAppConfig().getBasePackage() + ".";

		//TODO : currently only supports classes in base-package
		for(Class<?> cls : classes){
			boolean needLoad = cls.getName().startsWith(basePackage);
			for(String additionalPackage : container.getAppConfig().getAdditionalPackages()){
				if(needLoad){
					break;
				}
				needLoad = needLoad || cls.getName().startsWith(additionalPackage);
			}

            boolean isBean = Classes.isAnnotationPresent(cls, Bean.class);
            if (isBean && needLoad) {
                readBean(container, cls);
            }
		}
	}
	
	protected void readBean(BeanContainer container,Class<?> cls){
		Bean a = cls.getAnnotation(Bean.class);

		BeanDefinitionBase bd = new BeanDefinitionBase(Classes.getClassResourcePath(cls));
		bd.setAnnotation(true);
		if(!Strings.isEmpty(a.value())) {
			bd.setId(a.value());
		}else{
			
			if(!void.class.equals(a.type())){
				bd.setType(a.type());
			}else{
				bd.setType(resolveBeanType(cls,a));
			}
			
			if(!Strings.isEmpty(a.id())) {
				bd.setId(a.id());
			}

			if(!Strings.isEmpty(a.name())){
				bd.setName(a.name());
			}
			parseAdditionalTypeDef(bd,cls,a);
			bd.setBeanClass(cls);
			bd.setBeanClassType(BeanType.of(cls));
			bd.setPrimary(a.primary());
			bd.setSingleton(a.singleton());
			bd.setLazyInit(a.lazyInit());
			bd.setSortOrder(a.sortOrder());

			if(a.registerBeanFactory()){
				Class<?>[] targets = a.targetType();
				if(Arrays2.isNotEmpty(targets)){
					for(Class<?> target : targets){
						if(void.class.equals(target)){
							throw new BeanDefinitionException("Target type '" + target.getName() + "' not found, source : " + cls.getName());
						}
						bd.addFactoryBeanDef(new FactoryBeanDefinitionBase(target));
					}
				}
			}
			container.addBeanDefinition(bd);
		}
	}
	
	protected Class<?> resolveBeanType(Class<?> cls, Bean a) {
		if(!a.type().equals(void.class)){
			if(a.type().isAssignableFrom(cls)){
				throw new BeanDefinitionException(a.type() + " is not assignable from " + cls.getName() + ", source:"+cls.getName());
			}
			return a.type();
		}else if(cls.getInterfaces().length == 1){
			return cls.getInterfaces()[0];
		}
		return cls;
	}
	
	protected void parseAdditionalTypeDef(BeanDefinitionBase bd,Class<?> cls,Bean a){
		if(a.additionalTypeDef() != null && a.additionalTypeDef().length > 0){
			typeEach:
			for(Class<?> additional : a.additionalTypeDef()){
				for(TypeDefinition def : bd.getAdditionalTypeDefs()){
					if(def.getType() == additional){
						continue typeEach;
					}
				}
				if(!additional.isAssignableFrom(cls)){
					throw new BeanDefinitionException(additional.getName() + " is not assignable from " + cls.getName());
				}
				TypeDefinitionBase def = new TypeDefinitionBase();
				def.setType(additional);
				bd.addAdditionalTypeDef(def);
			}
		}
	}
	
	protected void addNotRepeatAdditionalTypeDef(BeanDefinitionBase bd, Class<?> cls){
		for(TypeDefinition def : bd.getAdditionalTypeDefs()){
			if(def.getType() == cls){
				return;
			}
		}
		TypeDefinitionBase def = new TypeDefinitionBase();
		def.setType(cls);
		bd.addAdditionalTypeDef(def);
	}
}
