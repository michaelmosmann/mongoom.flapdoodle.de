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

import java.lang.reflect.Field;

import de.flapdoodle.mongoom.mapping.ITypeConverter;

public class MappedAttribute
{
	private final ITypeConverter<?> _iConverter;
	private final Field _field;
	private final String _name;

	public MappedAttribute(Field field, String name, ITypeConverter<?> converter)
	{
		_field = field;
		_name = name;
		_iConverter = converter;
	}
	
	public ITypeConverter<?> getConverter()
	{
		return _iConverter;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public Field getField()
	{
		return _field;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_name == null) ? 0 : _name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		MappedAttribute other = (MappedAttribute) obj;
		if (_name == null)
		{
			if (other._name != null) return false;
		}
		else if (!_name.equals(other._name)) return false;
		return true;
	}
	
	
	
}
