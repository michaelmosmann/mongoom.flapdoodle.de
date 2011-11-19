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

package de.flapdoodle.mongoom.datastore;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.common.collect.Lists;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import de.flapdoodle.mongoom.annotations.Direction;
import de.flapdoodle.mongoom.mapping.index.FieldIndex;
import de.flapdoodle.mongoom.mapping.index.IndexDef;

public final class Indexes {

	private Indexes() {

	}

	public static void ensureIndex(DB db, IndexDef index, String collectionName) {

		// public <T> void ensureIndex(Class<T> clazz, String name,
		// Set<IndexFieldDef> defs, boolean unique,
		// boolean dropDupsOnCreate) {
		BasicDBObjectBuilder keys = BasicDBObjectBuilder.start();
		BasicDBObjectBuilder keyOpts = null;
		List<FieldIndex> indexSorted=Lists.newArrayList(index.fields());
		Collections.sort(indexSorted, new Comparator<FieldIndex>() {
			@Override
			public int compare(FieldIndex o1, FieldIndex o2) {
				if (o1.priority()==o2.priority()) return 0;
				if (o1.priority()<o2.priority()) return 1;
				return -1;
			}
		});
		
		for (FieldIndex def : indexSorted) {
			String fieldName = def.name();
			Direction dir = def.direction();
			if (dir == Direction.BOTH)
				keys.add(fieldName, 1).add(fieldName, -1);
			else
				keys.add(fieldName, (dir == Direction.ASC)
						? 1
						: -1);
		}

		String name = index.name();

		if (name != null && !name.isEmpty()) {
			if (keyOpts == null)
				keyOpts = new BasicDBObjectBuilder();
			keyOpts.add("name", name);
		}
		if (index.unique()) {
			if (keyOpts == null)
				keyOpts = new BasicDBObjectBuilder();
			keyOpts.add("unique", true);
			if (index.dropDups())
				keyOpts.add("dropDups", true);
		}
		if (index.sparse()) {
			if (keyOpts == null)
				keyOpts = new BasicDBObjectBuilder();
			keyOpts.add("sparse", true);
		}

		try {
			db.requestStart();
			DBCollection dbColl = db.getCollection(collectionName);
			DBObject indexKeys = keys.get();
//			DatastoreImpl._logger.info("Ensuring index for " + dbColl.getName() + "." + index + " with keys " + indexKeys);
			if (keyOpts == null) {
				DatastoreImpl._logger.info("Ensuring index for " + dbColl.getName() + "." + index + " with keys " + indexKeys);
				dbColl.ensureIndex(indexKeys);
			} else {
				DBObject options = keyOpts.get();
				DatastoreImpl._logger.info("Ensuring index for " + dbColl.getName() + "." + index + " with keys " + indexKeys
						+ " and opts " + options);
				dbColl.ensureIndex(indexKeys, options);
			}
		} finally {
			Errors.checkError(db, Operation.Insert);
			db.requestDone();
		}
	}
}
