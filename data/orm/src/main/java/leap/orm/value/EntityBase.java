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
package leap.orm.value;

import leap.lang.Named;
import leap.lang.accessor.Getter;
import leap.lang.accessor.NamedGetter;

import java.util.Set;

public interface EntityBase extends NamedGetter {

    /**
     * Returns the name of entity.
     */
	String getEntityName();

    /**
     * Returns all the field names in this entity.
     */
    Set<String> getFieldNames();

    /**
     * Sets the value of field.
     */
	<T> T set(String field,Object value);

    /**
     * Sets the value of field.
     */
	default <T> T set(Named field,Object value) {
        return set(field.getName(), value);
    }

}