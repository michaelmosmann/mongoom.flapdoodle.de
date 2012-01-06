/**
 * Copyright (C) 2010 Michael Mosmann <michael@mosmann.de>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.flapdoodle.mongoom;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogManager;

import com.google.inject.internal.Lists;

import de.flapdoodle.mongoom.datastore.Datastore;
import de.flapdoodle.mongoom.mapping.context.IMappingContext;
import de.flapdoodle.mongoom.mapping.context.IMappingContextFactory;
import de.flapdoodle.mongoom.mapping.context.MappingContext;
import de.flapdoodle.mongoom.mapping.context.MappingContextFactory;
import de.flapdoodle.mongoom.mapping.context.Transformations;


public abstract class AbstractDatastoreTest extends AbstractMongoOMTest {

	
	private final List<Class<?>> classes;
	private IDatastore datastore;

	public AbstractDatastoreTest(Class<?> clazz, Class<?>... classes) {
		ArrayList<Class<?>> all = Lists.newArrayList();
		all.add(clazz);
		if (classes!=null) all.addAll(Lists.newArrayList(classes));
		this.classes = all;
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		try {
		   LogManager.getLogManager().readConfiguration(getClass().getResourceAsStream("/logging.properties"));
		}catch (Exception e){
		   e.printStackTrace();
		}
		
		IMappingContextFactory<?> factory = newMappingContextFactory();
		Transformations transformations = new Transformations(factory,classes);
		IDatastore datastore=new Datastore(getMongo(), getDatabaseName(), transformations);
		datastore.ensureCaps();
		datastore.ensureIndexes();
		this.datastore=datastore;
	}

//	protected static void setLoggerLevel(Logger logger, Level level) {
//		System.out.println("Logger "+logger);
//		Handler[] handlers = logger.getHandlers();
//		for (Handler h : handlers) {
//			System.out.println("Handler "+h+" for "+logger);
//			h.setLevel(level);
//			h.setFilter(null);
//		}
//		Logger parent = logger.getParent();
//		if (parent!=null) {
//			setLoggerLevel(parent, level);
//		}
//	}
	protected IMappingContextFactory<?> newMappingContextFactory() {
		return new MappingContextFactory();
	}
	
	public IDatastore getDatastore() {
		return datastore;
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	
}
