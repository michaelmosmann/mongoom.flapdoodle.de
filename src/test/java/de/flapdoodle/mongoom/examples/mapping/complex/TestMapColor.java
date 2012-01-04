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

package de.flapdoodle.mongoom.examples.mapping.complex;

import java.awt.Color;
import java.util.List;

import de.flapdoodle.mongoom.AbstractDatastoreTest;
import de.flapdoodle.mongoom.IDatastore;
import de.flapdoodle.mongoom.mapping.context.IMappingContext;
import de.flapdoodle.mongoom.mapping.context.IMappingContextFactory;
import de.flapdoodle.mongoom.testlab.ColorMappingContext;


public class TestMapColor extends AbstractDatastoreTest {
	
	public TestMapColor() {
		super(ColorDocument.class);
	}
	
	@Override
	protected IMappingContextFactory<?> newMappingContextFactory() {
		return new IMappingContextFactory<IMappingContext>() {
			@Override
			public IMappingContext newContext() {
				return new ColorMappingContext();
			}
		};
	}
	
	public void testDocument() {
		
//		IMappingConfig mappingConfig = MappingConfig.getDefaults();
//		mappingConfig.getConverterFactories().add(new ColorConverterFactory());
//		ObjectMapper mongoom = new ObjectMapper(mappingConfig);
//		mongoom.map(ColorDocument.class);
//		
//
//		IDatastore datastore = mongoom.createDatastore(getMongo(), getDatabaseName());
//
//		datastore.ensureCaps();
//		datastore.ensureIndexes();

		IDatastore datastore=getDatastore();
		
		ColorDocument doc = new ColorDocument();
		doc.setName("Red");
		Color color = new Color(255,0,0);
		doc.setColor(color);
		datastore.save(doc);

		List<ColorDocument> list = datastore.with(ColorDocument.class).result().asList();
		assertEquals("One", 1, list.size());

		List<ColorDocument> views = datastore.with(ColorDocument.class).field("color","r").eq(255).result().asList();
		ColorDocument view = views.get(0);
		int red = view.getColor().getRed();
		assertEquals("Keywords", 255, red);
		
		views = datastore.with(ColorDocument.class).field("color").eq(color).result().asList();
		view = views.get(0);
		red = view.getColor().getRed();
		assertEquals("Keywords", 255, red);
	}

}
