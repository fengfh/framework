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
package leap.orm.dmo;

import leap.core.security.annotation.Ignore;
import leap.db.model.DbColumnBuilder;
import leap.db.model.DbTable;
import leap.db.model.DbTableBuilder;
import leap.junit.contexual.Contextual;
import leap.lang.beans.BeanProperty;
import leap.lang.beans.BeanType;
import leap.orm.OrmTestCase;
import leap.orm.annotation.NonColumn;
import leap.orm.mapping.EntityMapping;
import leap.orm.mapping.FieldMapping;
import leap.orm.tested.CreationEntity;

import leap.orm.tested.SelfRefEntity;
import org.junit.Test;

import java.sql.JDBCType;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class DmoTest extends OrmTestCase {
	
	@Test
	@Contextual
	public void testCreateEntity(){
		EntityMapping em = metadata.tryGetEntityMapping(CreationEntity.class);
		assertNull(em);
		assertTrue(dmo.cmdCreateEntity(CreationEntity.class).setDropTableIfExists(true).execute(true));
		em = metadata.getEntityMapping(CreationEntity.class);
		assertAllPropertiesMapped(CreationEntity.class,em);
		assertEquals(100,em.getFieldMapping("string1").getColumn().getLength());

		assertEquals(50,em.getFieldMapping("id").getColumn().getLength());

		assertFalse(em.getKeyFieldMappings().length == 0);
		assertEquals(1, em.getTable().getPrimaryKeyColumnNames().length);
		metadata.removeEntityMapping(em);
		db.cmdDropTable(em.getTable()).execute();
	}

	@Test
	public void testCreateSelfRefEntity() {
        EntityMapping em = metadata.tryGetEntityMapping(SelfRefEntity.class);
        assertNull(em);

	    dmo.cmdCreateEntity(SelfRefEntity.class).execute(false);
	    em = metadata.tryGetEntityMapping(SelfRefEntity.class);
	    assertNotNull(em);

	    metadata.removeEntityMapping(em);
    }

	@Test
	@Contextual
	public void testConcurrentCreateTable() throws InterruptedException {
		CountDownLatch cdl = new CountDownLatch(5);
		List<String> tableNames = new ArrayList<>(5);
		for(int i = 0; i < 5; i++){
			String tn = "table_"+i;
			tableNames.add(tn);
			if(db.checkTableExists(tn)){
				db.cmdDropTable(new DbTableBuilder(tn).getTableName()).execute();
			}
		}
		
		tableNames.forEach(s -> new Thread(() -> {
			try {
				DbTableBuilder builder = new DbTableBuilder(s);
				builder.addColumn(new DbColumnBuilder("id").setTypeName(JDBCType.VARCHAR.getName()).setLength(10));
				db.cmdCreateTable(builder.build()).execute();
			}finally {
				cdl.countDown();
			}
		}).start());
		
		cdl.await();
		
		tableNames.forEach(s -> {
			assertTrue(db.checkTableExists(s));
			db.cmdDropTable(new DbTableBuilder(s).getTableName()).execute();
		});
		
		tableNames.forEach(s -> assertFalse(db.checkTableExists(s)));
	}
	
	
	//TODO : ERROR - DmoTest.testCreateTable:51 » Metadata Entity's primary key(s) must not be empt
	/*
	@Test
	public void testCreateTable() {
	    EntityMapping em = metadata.tryGetEntityMapping(CreationEntity.class);
	    if(null == em) {
	        dmo.cmdCreateEntity(CreationEntity.class).execute();
	        em = metadata.getEntityMapping(CreationEntity.class);
	    }
	    if(db.checkTableExists(em.getTable())) {
	        db.cmdDropTable(em.getTable()).execute();
	    }
	    
	    assertTrue(dmo.createTableIfNotExists(CreationEntity.class));
	    assertFalse(dmo.createTableIfNotExists(CreationEntity.class));
	    assertTrue(db.checkTableExists(em.getTable()));
	    
	    metadata.removeEntityMapping(em);
	}
	*/
	
	private void assertAllPropertiesMapped(Class<?> cls,EntityMapping em){
		for(BeanProperty bp : BeanType.of(cls).getProperties()){
			if(bp.isAnnotationPresent(NonColumn.class)) {
				continue;
			}
			
			boolean mapped = false;
			for(FieldMapping pm : em.getFieldMappings()){
				if(pm.getBeanProperty() == bp){
					mapped = true;
					break;
				}
			}
			
			if(!mapped){
				fail("ConfigProperty '" + bp.getName() + "' not mapped in class '" + cls.getName() + "'");
			}
		}
	}
	
	
}