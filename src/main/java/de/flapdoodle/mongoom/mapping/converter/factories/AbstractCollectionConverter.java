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

package de.flapdoodle.mongoom.mapping.converter.factories;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.mapping.IContainerConverter;
import de.flapdoodle.mongoom.mapping.ITypeConverter;
import de.flapdoodle.mongoom.mapping.converter.generics.TypeExtractor;
import de.flapdoodle.mongoom.mapping.index.IndexDef;

public abstract class AbstractCollectionConverter<T extends Collection> implements ITypeConverter<T>,
		IContainerConverter<T> {

	protected ITypeConverter _converter;
	private Class<T> _collectionType;

	public AbstractCollectionConverter(ITypeConverter converter, Class<T> collectionType) {
		_converter = converter;
		_collectionType = collectionType;
	}

	@Override
	public T convertFrom(Object value) {
		T ret = createEmptyCollection();
		for (Object v : (Collection) value) {
			ret.add(_converter.convertFrom(v));
		}
		return (T) ret;
	}

	public Object convertTo(T value) {
		T ret = createEmptyCollection();
		for (Object v : value) {
			ret.add(_converter.convertTo(v));
		}
		return ret;
	}

	protected abstract T createEmptyCollection();

	@Override
	public List<IndexDef> getIndexes() {
		return null;
	}

	@Override
	public ITypeConverter<?> converter(String field) {
		throw new MappingException(List.class, "Fields not supported");
	}

	@Override
	public ITypeConverter<?> containerConverter() {
		return _converter;
	}

	@Override
	public boolean matchType(Class<?> entityClass, Class<?> type, Type genericType) {
		if (type.isAssignableFrom(_collectionType)) {
			Type parameterizedClass = TypeExtractor.getParameterizedClass(entityClass, genericType, 0);

			Class parameterizedType = null;
			Type genericSuperclass = null;

			if (parameterizedClass instanceof Class) {
				parameterizedType = (Class) parameterizedClass;
				genericSuperclass = parameterizedType.getGenericSuperclass();
			}
			if (parameterizedClass instanceof ParameterizedType) {
				ParameterizedType ptype = (ParameterizedType) parameterizedClass;
				parameterizedType = (Class) ptype.getRawType();
				genericSuperclass = ptype;
			}

			return _converter.matchType(entityClass, parameterizedType, genericSuperclass);
		}
		return false;
	}
}
