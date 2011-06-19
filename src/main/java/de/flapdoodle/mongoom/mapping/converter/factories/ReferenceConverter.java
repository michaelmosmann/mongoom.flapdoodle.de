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

package de.flapdoodle.mongoom.mapping.converter.factories;

import java.lang.reflect.Type;
import java.util.List;

import org.bson.types.ObjectId;

import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.mapping.ITypeConverter;
import de.flapdoodle.mongoom.mapping.converter.annotations.NothingAnnotated;
import de.flapdoodle.mongoom.mapping.converter.generics.TypeExtractor;
import de.flapdoodle.mongoom.mapping.index.IndexDef;
import de.flapdoodle.mongoom.types.Reference;

public class ReferenceConverter<T extends Reference> implements ITypeConverter<T>//,IContainerConverter<T>
{

	RawConverter<ObjectId> _converter;
	private final Class<?> _type;

	public ReferenceConverter(Class<?> type) {
		_type = type;
		_converter = new RawConverter<ObjectId>(ObjectId.class,new NothingAnnotated());
	}

	@Override
	public T convertFrom(Object value) {
		ObjectId id = _converter.convertFrom(value);
		return (T) Reference.getInstance(_type, id);
	}

	public Object convertTo(T value) {
		if (value==null) return null;
		
		if (!_type.isAssignableFrom(value.getType()))
			throw new MappingException(_type, "Reference " + value + " is not of Type " + _type);
		return value.getId();
	};

	@Override
	public List<IndexDef> getIndexes() {
		return null;
	}

	@Override
	public ITypeConverter<?> converter(String field) {
		throw new MappingException(List.class, "Fields not supported");
	}

	@Override
	public boolean matchType(Class<?> entityClass, Class<?> type, Type genericType) {
		if (type.isAssignableFrom(Reference.class)) {
			Type parameterizedClass = TypeExtractor.getParameterizedClass(entityClass, genericType, 0);
			if ((parameterizedClass instanceof Class) && ((Class) parameterizedClass).isAssignableFrom(_type)) {
				return true;
			}
		}
		return false;
	}

	//	@Override
	//	public ITypeConverter<?> containerConverter()
	//	{
	//		return _converter;
	//	}

	@Override
	public String toString() {
		return getClass() + "(" + _type + ")";
	}
}
