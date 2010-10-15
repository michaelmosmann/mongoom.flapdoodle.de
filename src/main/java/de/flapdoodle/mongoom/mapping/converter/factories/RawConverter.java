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

public class RawConverter<T> implements ITypeConverter<T>
{
	private final Class<?> _type;

	public RawConverter(Class<?> type)
	{
		_type = type;
	}
	
	@Override
	public ITypeConverter<?> converter(String field)
	{
		throw new MappingException(List.class,"Fields not supported");
	}
	
	@Override
	public T convertFrom(Object value)
	{
		if (_type.isAssignableFrom(value.getClass())) return (T) value;
		throw new MappingException("Could not convert "+value.getClass()+" to "+_type);
	}
	
	public Object convertTo(T value)
	{
		return value;
	};
	
	@Override
	public List<IndexDef> getIndexes()
	{
		return null;
	}
	
	@Override
	public boolean matchType(Class<?> entityClass, Class<?> type, Type genericType)
	{
		return type.isAssignableFrom(_type);
	}
	
	@Override
	public String toString()
	{
		return getClass()+"("+_type+")";
	}
}
