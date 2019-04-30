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
package leap.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE}) 
@Retention(RetentionPolicy.RUNTIME)
public @interface Bean {
	
	/**
	 * The id of bean.
	 */
	String value() default "";
	
	/**
	 * The id of bean.
	 */
	String id() default "";

	/**
	 * The name of bean.
	 */
	String name() default "";
	
	/**
	 * The type of bean.
	 */
	Class<?> type() default void.class;

	/**
	 * Is primary bean.
	 */
	boolean primary() default false;
	
	/**
	 * Is singleton instance.
	 */
	boolean singleton() default true;
	
	/**
	 * Is lazy init.
	 */
	boolean lazyInit() default true;

	/**
	 * addition type def
	 */
	Class<?>[] additionalTypeDef() default {};

	/**
	 * sort order
	 */
	float sortOrder() default 100;

	/**
	 * register this bean as a bean factory
	 */
	boolean registerBeanFactory() default false;

	/**
	 * this factory will create target type
	 */
	Class<?>[] targetType() default {};
}