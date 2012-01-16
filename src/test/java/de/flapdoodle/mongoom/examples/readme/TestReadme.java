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

package de.flapdoodle.mongoom.examples.readme;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.inject.internal.Sets;
import com.mongodb.Mongo;

import de.flapdoodle.mongoom.AbstractMongoOMTest;
import de.flapdoodle.mongoom.IDatastore;
import de.flapdoodle.mongoom.datastore.Datastore;
import de.flapdoodle.mongoom.mapping.context.IMappingContextFactory;
import de.flapdoodle.mongoom.mapping.context.MappingContextFactory;
import de.flapdoodle.mongoom.mapping.context.Transformations;
import de.flapdoodle.mongoom.mapping.properties.Property;
import de.flapdoodle.mongoom.mapping.properties.PropertyReference;

public class TestReadme extends AbstractMongoOMTest {

	public void testFull() {

		//Mongo mongo = new Mongo( "localhost" , 12345 );
		Mongo mongo = getMongo();

		Set<Class<?>> classes = Sets.newLinkedHashSet();
		classes.add(Document.class);

		IMappingContextFactory<?> factory = new MappingContextFactory();
		Transformations transformations = new Transformations(factory, classes);
		IDatastore datastore = new Datastore(mongo, "databaseName", transformations);

		datastore.ensureCaps();
		datastore.ensureIndexes();

		Document doc = new Document();
		doc.setCreated(new Date());
		doc.setName("Document One");

		Meta metaInfo = new Meta();
		ArrayList<String> keywords = Lists.newArrayList("Document", "One");
		metaInfo.setKeywords(keywords);

		doc.setMeta(metaInfo);
		datastore.save(doc);

		List<Document> list = datastore.with(Document.class).result().asList();

		List<DocumentView> views = datastore.with(Document.class).field(Document.Meta).listfield(Meta.
				Keywords).eq("One").withView(DocumentView.class).asList();
		DocumentView view = views.get(0);

		assertEquals("Equal", keywords, view.getKeywords());
	}
}
