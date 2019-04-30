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
package leap.orm.mapping;

import leap.lang.Args;

public class JoinFieldMapping {

	protected final String  localFieldName;
    protected final boolean localPrimaryKey;
    protected final String  referencedEntityName;
	protected final String  referencedFieldName;
	
	public JoinFieldMapping(String localFieldName, boolean localPrimaryKey, String referencedEntityName, String referencedFieldName) {
		Args.notEmpty(localFieldName,"localFieldName");
		Args.notEmpty(referencedFieldName,"referencedFieldName");
		
		this.localFieldName       = localFieldName;
        this.localPrimaryKey      = localPrimaryKey;
        this.referencedEntityName = referencedEntityName;
		this.referencedFieldName  = referencedFieldName;
	}

	public String getLocalFieldName() {
		return localFieldName;
	}

    /**
     * For many-to-many only.
     */
    public String getReferencedEntityName() {
        return referencedEntityName;
    }

    public String getReferencedFieldName() {
		return referencedFieldName;
	}

    public boolean isLocalPrimaryKey() {
        return localPrimaryKey;
    }
}
