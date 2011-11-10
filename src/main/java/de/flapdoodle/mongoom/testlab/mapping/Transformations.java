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

package de.flapdoodle.mongoom.testlab.mapping;

import java.util.List;
import java.util.Map;

import com.google.inject.internal.Maps;
import com.mongodb.DBObject;

import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.testlab.IEntityTransformation;
import de.flapdoodle.mongoom.testlab.ITransformation;
import de.flapdoodle.mongoom.testlab.entities.EntityVisitor;

public class Transformations {

	Map<Class<?>, IEntityTransformation<?>> _transformations = Maps.newHashMap();
	private final IMappingContextFactory<?> _contextFactory;
	private final List<Class<?>> _entityTypes;

	public Transformations(IMappingContextFactory<?> contextFactory, List<Class<?>> entityTypes) {
		_contextFactory = contextFactory;
		_entityTypes = entityTypes;
		for (Class<?> type : _entityTypes) {
			map(type);
		}
	}

	protected <T> void map(Class<T> entityType) {
		IMappingContext mappingContext = _contextFactory.newContext();
		EntityVisitor<T> entityVisitor = new EntityVisitor<T>();
		IEntityTransformation<T> transformation = entityVisitor.transformation(mappingContext, entityType);
		if (_transformations.put(entityType, transformation) != null) {
			throw new MappingException(entityType, "allready mapped");
		}
	}

	public <T> IEntityTransformation<T> transformation(Class<T> type) {
		return (IEntityTransformation<T>) _transformations.get(type);
	}
}
