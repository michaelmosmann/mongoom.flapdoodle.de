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

package de.flapdoodle.mongoom.examples.minimal;

import java.util.List;

import de.flapdoodle.mongoom.AbstractMongoOMTest;
import de.flapdoodle.mongoom.IDatastore;
import de.flapdoodle.mongoom.ObjectMapper;

public class TestMinimal extends AbstractMongoOMTest {

	public void testDocument() {
		ObjectMapper mongoom = new ObjectMapper();
		mongoom.map(Document.class);

		IDatastore datastore = mongoom.createDatastore(getMongo(), getDatabaseName());

		datastore.ensureCaps();
		datastore.ensureIndexes();

		Document doc = new Document();
		datastore.save(doc);

		List<Document> list = datastore.with(Document.class).result().asList();
		assertEquals("One", 1, list.size());
	}
}
