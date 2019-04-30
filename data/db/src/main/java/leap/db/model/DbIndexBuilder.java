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
package leap.db.model;

import leap.lang.Args;
import leap.lang.Buildable;
import leap.lang.Collections2;
import leap.lang.json.JsonArray;
import leap.lang.json.JsonObject;
import leap.lang.json.JsonParsable;
import leap.lang.json.JsonValue;

import java.util.ArrayList;
import java.util.List;

public class DbIndexBuilder implements Buildable<DbIndex>,JsonParsable {
	
	protected String       name;
	protected boolean      unique;
	protected List<String> columnNames = new ArrayList<>();
	protected boolean	   internal;

    public DbIndexBuilder() {

    }

    public DbIndexBuilder(String name) {
        this.name = name;
    }

    public DbIndexBuilder(DbIndex index) {
        this.name = index.getName();
        this.unique = index.isUnique();
        Collections2.addAll(columnNames,index.getColumnNames());
    }

    /**
     * Required. The full name of index.
     */
    public String getName() {
		return name;
	}

	public DbIndexBuilder setName(String name) {
		this.name = name;
		return this;
	}

    public boolean isUnique() {
		return unique;
	}

	public DbIndexBuilder setUnique(boolean unique) {
		this.unique = unique;
		return this;
	}
	
	public boolean isInternal() {
		return internal;
	}

	public DbIndexBuilder setInternal(boolean internal) {
		this.internal = internal;
		return this;
	}

	public List<String> getColumnNames() {
		return columnNames;
	}

	public DbIndexBuilder addColumnName(String columnName){
		return addColumnName(columnName, -1);
	}
	
	public DbIndexBuilder addColumnName(String columnName,int index){
		Args.notEmpty(columnName);
		if(index < 0){
			columnNames.add(columnName);
		}else{
			columnNames.add(index,columnName);
		}
		return this;
	}

	public boolean matchUnique(DbIndexBuilder ix) {
        if(unique != ix.unique) {
            return false;
        }
        if(columnNames.size() != ix.columnNames.size()) {
            return false;
        }
        for(int i=0;i<columnNames.size();i++) {
            if(!columnNames.get(i).equalsIgnoreCase(ix.columnNames.get(i))) {
                return false;
            }
        }
        return true;
    }
	
	@Override
    public DbIndex build() {
	    return new DbIndex(name, unique, columnNames.toArray(new String[columnNames.size()]));
    }

	@Override
    public void parseJson(JsonValue value) {
		JsonObject o = value.asJsonObject();
		
		this.name   = o.getString("name");
		this.unique = o.getBoolean("unique",false);
		
		JsonArray columns = o.getArray("columnNames");
		if(null != columns){
			for(int i=0;i<columns.length();i++){
				addColumnName(columns.getString(i));
			}
		}
    }
}