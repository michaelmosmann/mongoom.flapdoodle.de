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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

import com.mongodb.DBObject;

import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.logging.LogConfig;
import de.flapdoodle.mongoom.mapping.ITypeConverter;
import de.flapdoodle.mongoom.mapping.Mapper;
import de.flapdoodle.mongoom.mapping.converter.reflection.ClassAndFields;

public abstract class AbstractReadOnlyConverter<T>
{
	private static final Logger _logger = LogConfig.getLogger(AbstractReadOnlyConverter.class);
	
	private final Class<T> _entityClass;
	private final Constructor<T> _constructor;

	public AbstractReadOnlyConverter(Mapper mapper, Class<T> entityClass)
	{
		_entityClass = entityClass;
		_constructor = ClassAndFields.getConstructor(_entityClass);

		_logger.severe("Map "+_entityClass);
		

	}
	
	protected Class<T> getEntityClass()
	{
		return _entityClass;
	}
	
	protected T newInstance()
	{
		try
		{
			return _constructor.newInstance();
		}
		catch (IllegalArgumentException e)
		{
			throw new MappingException(_entityClass,"newInstance",e);
		}
		catch (InstantiationException e)
		{
			throw new MappingException(_entityClass,"newInstance",e);
		}
		catch (IllegalAccessException e)
		{
			throw new MappingException(_entityClass,"newInstance",e);
		}
		catch (InvocationTargetException e)
		{
			throw new MappingException(_entityClass,"newInstance",e);
		}
	}
	
	protected static <T> void setEntityField(T instance, MappedAttribute attribute, DBObject dbobject)
	{
		try
		{
			Field field = attribute.getField();
			Object fieldValue = dbobject.get(attribute.getName());
	//				Object fieldValue = field.get(entity);
			if (fieldValue!=null)
			{
				ITypeConverter converter = attribute.getConverter();
				field.set(instance, converter.convertFrom(fieldValue));
			}
		}
		catch (MappingException e)
		{
			throw new MappingException(instance.getClass(),e);
		}
		catch (IllegalArgumentException e)
		{
			throw new MappingException(instance.getClass(),e);
		}
		catch (IllegalAccessException e)
		{
			throw new MappingException(instance.getClass(),e);
		}
	}

}
