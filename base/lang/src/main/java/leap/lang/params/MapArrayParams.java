/*
 * Copyright 2015 the original author or authors.
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
package leap.lang.params;

import leap.lang.collection.WrappedCaseInsensitiveMap;

import java.util.HashMap;
import java.util.Map;

public class MapArrayParams extends NamedParamsBase {
	
	protected final int		 start;
	protected final Object[] values;

	public MapArrayParams(Map<String, Object> map, Object... values) {
        super(WrappedCaseInsensitiveMap.create(null == map ? new HashMap<>(1) : map));
		this.start  = map.size();
		this.values = values;
		for(int i=0;i<values.length;i++){
			this.map.put(ArrayParams.PREFIX + (start + i), values[i]);
		}
	}
	
	public Object[] array() {
		return values;
	}
	
	@Override
    public boolean isEmpty() {
	    return map.isEmpty();
    }

	@Override
    public boolean isIndexed() {
	    return true;
    }

	@Override
	public boolean isArray() {
		return true;
	}

	@Override
    public Object get(int i) throws IllegalStateException {
	    return values[i];
    }
	
}
