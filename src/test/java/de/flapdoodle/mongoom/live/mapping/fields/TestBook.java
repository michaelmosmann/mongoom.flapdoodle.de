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

package de.flapdoodle.mongoom.live.mapping.fields;

import de.flapdoodle.mongoom.AbstractMongoOMTest;
import de.flapdoodle.mongoom.IDatastore;
import de.flapdoodle.mongoom.ObjectMapper;
import de.flapdoodle.mongoom.live.beans.fields.Book;

public class TestBook extends AbstractMongoOMTest {

	private IDatastore _datastore;

	public void testQuery() {
		_datastore.insert(Book.getInstance("Das Leben", "sommer", "sonne", "sachbuch"));
		_datastore.insert(Book.getInstance("Die Arbeit", "sachbuch"));
		_datastore.insert(Book.getInstance("Das Hobby", "sachbuch", "sonne"));
		_datastore.insert(Book.getInstance("Dei Freizeit", "sommer"));

		assertEquals("sommer", 2, _datastore.with(Book.class).field("category").eq("sommer").result().countAll());

		Book book = _datastore.with(Book.class).field("name").eq("Das Leben").result().get();
		assertNotNull("Book", book);
		//		book.setCategory(Lists.newArrayList("blau","wal"));
		_datastore.update(book);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		ObjectMapper mongoom = new ObjectMapper();
		mongoom.map(Book.class);

		IDatastore datastore = mongoom.createDatastore(getMongo(), getDatabaseName());

		datastore.ensureCaps();
		datastore.ensureIndexes();

		_datastore = datastore;
	}

//	@Override
//	protected boolean cleanUpAfterTest() {
//		return false;
//	}
}
