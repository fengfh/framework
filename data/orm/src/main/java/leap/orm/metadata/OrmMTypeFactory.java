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
package leap.orm.metadata;

import java.lang.reflect.Type;

import leap.core.annotation.Inject;
import leap.lang.Args;
import leap.lang.Enums;
import leap.lang.meta.*;
import leap.orm.OrmContext;
import leap.orm.mapping.EntityMapping;
import leap.orm.mapping.FieldMapping;
import leap.orm.model.Model;

public class OrmMTypeFactory implements MTypeFactory {
	
	protected @Inject OrmContext[] ormContexts;
	
	@Override
	public MType getMType(Class<?> type, Type genericType, MTypeContext context) {
        Args.notNull(context.root(), "Root factory must be exists!");

        for(OrmContext c : ormContexts) {
			EntityMapping em = c.getMetadata().tryGetEntityMapping(type);
			
			if(null != em) {
				return getMType(type, genericType, context, c, em);
			}
			
		}

		return null;
	}

	protected MType getMType(Class<?> type, Type genericType, MTypeContext context, OrmContext c,  EntityMapping em) {
		MComplexTypeBuilder ct = new MComplexTypeBuilder();

		ct.setName(em.getEntityName());

        MTypeFactory root = context.root();

		for(FieldMapping fm : em.getFieldMappings()) {
			MPropertyBuilder p = new MPropertyBuilder();
			p.setName(fm.getFieldName());
			p.setType(root.getMType(fm.getJavaType()));
			p.setLength(fm.getMaxLength());
			p.setNullable(fm.isNullable());
			p.setPrecision(fm.getPrecision());
			p.setScale(fm.getScale());
            p.setSortable(fm.getSortable());
            p.setFilterable(fm.getFilterable());

            if(type.isEnum()) {
                p.setEnumValues(Enums.getValues(type));
            }
			
			ct.addProperty(p.build());
		}
		
		return ct.build();
	}

}
