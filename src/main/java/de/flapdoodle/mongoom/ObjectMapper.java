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

import java.util.Collections;
import java.util.Set;

import com.mongodb.Mongo;

import de.flapdoodle.mongoom.datastore.DatastoreImpl;
import de.flapdoodle.mongoom.mapping.IMappingConfig;
import de.flapdoodle.mongoom.mapping.Mapper;
import de.flapdoodle.mongoom.mapping.MappingConfig;

public class ObjectMapper {

	private final Mapper _mapper;

	public ObjectMapper() {
		this(MappingConfig.getDefaults(),Collections.EMPTY_SET);
	}

	@Deprecated
	public ObjectMapper(Set<Class> entityClasses) {
		_mapper = new Mapper();
		for (Class m : entityClasses) {
			_mapper.map(m);
		}
	}

	public ObjectMapper(IMappingConfig mappingConfig) {
		this(mappingConfig,Collections.EMPTY_SET);
	}
	
	public ObjectMapper(IMappingConfig mappingConfig,Set<Class> entityClasses) {
		_mapper = new Mapper(mappingConfig);
		for (Class m : entityClasses) {
			_mapper.map(m);
		}
	}
	
	public synchronized ObjectMapper map(Class entityClass) {
		_mapper.map(entityClass);
		return this;
	}

	public IDatastore createDatastore(Mongo m, String name) {
		return new DatastoreImpl(_mapper, m, name);
	}
}
