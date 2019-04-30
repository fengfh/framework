/*
 * Copyright 2013 the original author or authors.
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

import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.METHOD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@AInject
public @interface Inject {
	
	/**
	 * Same as {@link #name()}.
	 */
	String value() default "";

	/**
	 * The bean id depends on.
	 */
	String id() default "";
	
	/**
	 * The bean type depends on.
	 */
	Class<?> type() default Object.class;
	
	/**
	 * The bean name depends on, must be used with type.
	 */
	String name() default "";
	
	/**
	 * Inject the primary bean if this value set to <code>true</code> and the named bean not found.
	 * 
	 * <p>
	 * Only use in single bean injection.
	 */
	boolean primary() default false;
	
	/**
	 * The bean qualifier value , only use in bean list injection.
	 */
	String qualifier() default "";

    /**
     * Creates a new instance if the bean not found.
     */
    boolean create() default false;
}