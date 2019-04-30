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
package leap.lang.accessor;

import java.util.LinkedHashMap;
import java.util.Map;

public class MapAttributeAccessor implements AttributeAccessor {
	
	protected final Map<String,Object> attributes;
	
	public MapAttributeAccessor(){
		this.attributes = new LinkedHashMap<>();
	}
	
	protected MapAttributeAccessor(Map<String,Object> map){
		this.attributes = null == map ? new LinkedHashMap<>() : map;
	}

    public Map<String, Object> getAttributes(){
        return attributes;
    }

	@Override
    public Object getAttribute(String name) {
	    return attributes.get(name);
    }

	@Override
    public void setAttribute(String name, Object value) {
		attributes.put(name, value);
    }

	@Override
    public void removeAttribute(String name) {
		attributes.remove(name);
    }
}