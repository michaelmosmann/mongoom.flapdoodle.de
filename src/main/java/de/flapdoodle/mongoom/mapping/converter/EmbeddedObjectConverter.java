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

package de.flapdoodle.mongoom.mapping.converter;

import com.mongodb.DBObject;

import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.mapping.ITypeConverter;
import de.flapdoodle.mongoom.mapping.Mapper;
import de.flapdoodle.mongoom.mapping.MappingContext;

public class EmbeddedObjectConverter<T> extends AbstractObjectConverter<T> implements ITypeConverter<T> {

	public EmbeddedObjectConverter(Mapper mapper, MappingContext<?> context, Class<T> entityClass) {
		super(mapper, context, entityClass);
	}

	@Override
	public Object convertTo(T fieldValue) {
		return convertEntityToDBObject(fieldValue);
	}

	@Override
	public T convertFrom(Object value) {
		if (value instanceof DBObject) {
			return convertDBObjectToEntity((DBObject) value);
		}
		throw new MappingException("Could not convert " + value + " to " + getEntityClass());
	}

}
