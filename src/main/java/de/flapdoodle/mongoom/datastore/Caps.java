/**
 * Copyright (C) 2010 Michael Mosmann <michael@mosmann.de>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
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

public final class Caps {

	private Caps() {

	}

	public static void ensureCaps(DB db, Class<?> entity) {
		Entity annotation = entity.getAnnotation(Entity.class);
		CappedAt cap = annotation.cap();
		if ((cap != null) && (cap.count() > 0)) {
			try {
				db.requestStart();
				String collName = annotation.value();
				BasicDBObjectBuilder dbCapOpts = BasicDBObjectBuilder.start("capped", true);
				if (cap.value() > 0)
					dbCapOpts.add("size", cap.value());
				if (cap.count() > 0)
					dbCapOpts.add("max", cap.count());
				DBCollection dbColl = db.getCollection(collName);

				if (db.getCollectionNames().contains(collName)) {
					DBObject dbResult = db.command(BasicDBObjectBuilder.start("collstats", collName).get());
					if (dbResult.containsField("capped")) {
						// TODO: check the cap options.
						DatastoreImpl._logger.warning("DBCollection already exists is cap'd already; doing nothing. " + dbResult);
					} else {
						DatastoreImpl._logger.warning("DBCollection already exists with same name(" + collName
								+ ") and is not cap'd; not creating cap'd version!");
					}
				} else {
					db.createCollection(collName, dbCapOpts.get());
					DatastoreImpl._logger.info("Created cap'd DBCollection (" + collName + ") with opts " + cap);
				}
			} finally {
				Errors.checkError(db, Operation.Insert);
				db.requestDone();
			}
		}
	}
}
