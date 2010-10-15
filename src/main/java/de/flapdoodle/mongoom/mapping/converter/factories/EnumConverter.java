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

import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.mapping.ITypeConverter;
import de.flapdoodle.mongoom.mapping.index.IndexDef;

public class EnumConverter<T extends Enum<T>> implements ITypeConverter<T>
{
	private final Class<T> _enumClass;

	public EnumConverter(Class<T> enumClass)
	{
		_enumClass = enumClass;
	}
	
	@Override
	public List<IndexDef> getIndexes()
	{
		return null;
	}
	
	public Object convertTo(T value)
	{
		return value.name();
	};
	
	@Override
	public T convertFrom(Object value)
	{
		if (value instanceof String) return Enum.valueOf(_enumClass, (String)value);
		throw new MappingException("Could not convert "+value+" to "+_enumClass);
	}
	
	@Override
	public ITypeConverter<?> converter(String field)
	{
		throw new MappingException(_enumClass,"Fields not supported");
	}
	
	@Override
	public boolean matchType(Class<?> entityClass, Class<?> type, Type genericType)
	{
		return type.isAssignableFrom(_enumClass);
	}
}
