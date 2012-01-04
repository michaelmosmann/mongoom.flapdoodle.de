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

package de.flapdoodle.mongoom.examples.callbacks;

import java.util.List;

import de.flapdoodle.mongoom.AbstractDatastoreTest;
import de.flapdoodle.mongoom.IDatastore;

public class TestCallbacks extends AbstractDatastoreTest {

	
	public TestCallbacks() {
		super(Stats.class);
	}
	
	public void testReadWrite() {
//		ObjectMapper mongoom = new ObjectMapper();
//		mongoom.map(Stats.class);
//
//		IDatastore datastore = mongoom.createDatastore(getMongo(), getDatabaseName());
//		datastore.ensureCaps();
//		datastore.ensureIndexes();

		IDatastore datastore = getDatastore();
		
		Stats doc = new Stats();
		doc.setA(2);
		doc.setB(3);
		datastore.save(doc);

		List<Stats> list = datastore.with(Stats.class).result().asList();
		assertEquals("One", 1, list.size());
		assertEquals("C", Integer.valueOf(5), list.get(0).getReadC());
	}

//	@Override
//	protected boolean cleanUpAfterTest() {
//		return false;
//	}
}
