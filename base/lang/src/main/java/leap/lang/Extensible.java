/*
 *
 *  * Copyright 2016 the original author or authors.
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

package leap.lang;

import java.util.Map;

public interface Extensible extends ExtensibleGetter {

    /**
     * Returns all the extensions.
     */
    Map<Class<?>, Object> getExtensions();

    /**
     * Set extension.
     */
    default <T> void setExtension(T extension) {
        setExtension(extension.getClass(), extension);
    }

    /**
     * Set extension.
     */
    <T> void setExtension(Class<T> type, Object extension);

    /**
     * Removes the extension of the type and returns the extension object.
     */
    <T> T removeExtension(Class<?> type);
}