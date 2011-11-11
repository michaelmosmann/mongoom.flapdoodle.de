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

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import de.flapdoodle.mongoom.annotations.CappedAt;
import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.exceptions.ObjectMapperException;
import de.flapdoodle.mongoom.testlab.datastore.collections.ICollection;
import de.flapdoodle.mongoom.testlab.datastore.collections.ICollectionCap;

public final class Caps {

	private Caps() {

	}

	@Deprecated
	public static void ensureCaps(DB db, Class<?> entity) {
		Entity annotation = entity.getAnnotation(Entity.class);
		String collName = annotation.value();
		CappedAt cap = annotation.cap();
		if (cap!=null) {
			ensureCaps(db, collName, ICollectionCap.Annotated.with(cap));
		}
	}
	
	public static void ensureCaps(DB db, ICollection collection) {
		ensureCaps(db, collection.name(),collection.cap());
	}
	
	public static void ensureCaps(DB db, String collectionName, ICollectionCap capCollection) {
		
		if (capCollection != null) {
			long count = capCollection.count();
			long size = capCollection.size();
			
			if ((size==0) && (count==0)) throw new ObjectMapperException("Size and Count == 0");
			if ((size<Long.MAX_VALUE) && (count<Long.MAX_VALUE)) {
				try {
					db.requestStart();
					BasicDBObjectBuilder dbCapOpts = BasicDBObjectBuilder.start("capped", true);
					if (size > 0)
						dbCapOpts.add("size", size);
					if (count > 0)
						dbCapOpts.add("max", count);
					DBCollection dbColl = db.getCollection(collectionName);
	
					if (db.getCollectionNames().contains(collectionName)) {
						DBObject dbResult = db.command(BasicDBObjectBuilder.start("collstats", collectionName).get());
						if (dbResult.containsField("capped")) {
							// TODO: check the cap options.
							DatastoreImpl._logger.warning("DBCollection already exists is cap'd already; doing nothing. " + dbResult);
						} else {
							DatastoreImpl._logger.warning("DBCollection already exists with same name(" + collectionName
									+ ") and is not cap'd; not creating cap'd version!");
						}
					} else {
						db.createCollection(collectionName, dbCapOpts.get());
						DatastoreImpl._logger.info("Created cap'd DBCollection (" + collectionName + ") with opts " + capCollection);
					}
				} finally {
					Errors.checkError(db, Operation.Insert);
					db.requestDone();
				}
			}
		}
	}
}
