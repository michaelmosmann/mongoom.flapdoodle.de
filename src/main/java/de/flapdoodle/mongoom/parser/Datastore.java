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

package de.flapdoodle.mongoom.parser;

import java.util.List;

import com.mongodb.DB;
import com.mongodb.Mongo;

import de.flapdoodle.mongoom.IDatastore;
import de.flapdoodle.mongoom.IEntityQuery;
import de.flapdoodle.mongoom.parser.converter.ObjectMapper;

public class Datastore implements IDatastore {

	private ObjectMapper _objectMapper;
	private Mongo _mongo;
	private String _name;
	private DB _db;

	public Datastore(ObjectMapper objectMapper, Mongo mongo, String name) {
		_objectMapper = objectMapper;
		_mongo = mongo;
		_name = name;

		_db = _mongo.getDB(_name);
	}

	@Override
	public void ensureCaps() {
		notImplemented();
	}

	@Override
	public void ensureIndexes() {
		notImplemented();
	}

	@Override
	public <T> void insert(T entity) {
		notImplemented();
	}

	@Override
	public <T> void save(T entity) {
		notImplemented();
	}

	@Override
	public <T> void update(T entity) {
		notImplemented();
	}

	@Override
	public <T> void delete(T entity) {
		notImplemented();
	}

	@Override
	public <T> List<T> find(Class<T> entityClass) {
		notImplemented();
		return null;
	}

	@Override
	public <T> IEntityQuery<T> with(Class<T> entityClass) {
		notImplemented();
		return null;
	}

	private static void notImplemented() {
		throw new IllegalArgumentException("not implemented");
	}
}
