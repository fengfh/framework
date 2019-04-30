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
package leap.lang.enums;

import leap.lang.Valued;

public enum Bool implements Valued<Boolean>{

	TRUE(true),
	
	FALSE(false),
	
	NONE(null);
	
	private final Boolean value;
	
	Bool(Boolean value){
		this.value = value;
	}

    public Boolean value() {
        return value;
    }

	@Override
    public Boolean getValue() {
	    return value;
    }
	
	public boolean isTrue(){
		return value == true;
	}
	
	public boolean isFalse(){
		return value == false;
	}
	
	public boolean isNone(){
		return value == null;
	}

    public boolean isPresent() {
        return value != null;
    }
}
