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
package leap.lang.convert;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import leap.lang.Out;
import leap.lang.Types;
import leap.lang.reflect.Reflection;

@SuppressWarnings("rawtypes")
public class MapConverter extends AbstractConverter<Map> {

    @Override
    @SuppressWarnings("unchecked")
    public boolean convertFrom(Object value, Class<?> targetType, Type genericType, Out<Object> out, ConvertContext context) throws Throwable {
		if(value instanceof Map){
			Map map = (Map)value;
			
			if(null == genericType){
				out.set(value);
				return true;
			}

			out.set(doConvert(context, map, targetType, genericType));
			return true;
		}
		return false;
    }
    
    protected static Map createMap(Class<?> mapType){
    	if(mapType.equals(Map.class)){
    		return new LinkedHashMap();
    	}
    	return (Map)Reflection.newInstance(mapType);
    }

    protected static Map doConvert(ConvertContext context, Map from, Class<?> targetType, Type genericType) {
        Type[] typeArguments = Types.getTypeArguments(genericType);

        Class<?> keyType = Types.getActualType(typeArguments[0]);
        Class<?> valType = Types.getActualType(typeArguments[1]);

        Map to = createMap(targetType);

        for(Object o : from.entrySet()){
            Entry entry = (Entry)o;

            Object key = Converts.convert(entry.getKey(),   keyType, typeArguments[0], context);
            Object val = Converts.convert(entry.getValue(), valType, typeArguments[1], context);

            to.put(key,val);
        }

        return to;
    }

    public static final class ConcreteMapConverter extends AbstractConverter<Map> {
        @Override
        public boolean convertFrom(Object value, Class<?> targetType, Type genericType, Out<Object> out, ConvertContext context) throws Throwable {
            if(value instanceof Map) {
                Map from = (Map)value;
                Map to;

                if(null == genericType) {
                    to = (Map)Reflection.newInstance(targetType);
                    to.putAll(from);
                }else {
                    to = doConvert(context, from, targetType, genericType);
                }
                out.set(to);
                return true;
            }
            return false;
        }
    }
}
