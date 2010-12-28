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
import java.lang.reflect.Method;
import java.util.logging.Logger;

import com.mongodb.DBObject;

import de.flapdoodle.mongoom.annotations.OnRead;
import de.flapdoodle.mongoom.annotations.OnWrite;
import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.exceptions.ObjectMapperException;
import de.flapdoodle.mongoom.logging.LogConfig;
import de.flapdoodle.mongoom.mapping.ITypeConverter;
import de.flapdoodle.mongoom.mapping.Mapper;
import de.flapdoodle.mongoom.mapping.MappingContext;
import de.flapdoodle.mongoom.mapping.converter.reflection.ClassInformation;

public abstract class AbstractReadOnlyConverter<T> {

	private static final Logger _logger = LogConfig.getLogger(AbstractReadOnlyConverter.class);

	private final Class<T> _entityClass;
	private final Constructor<T> _constructor;

	private final Method _onReadCallback;
	private final Method _onWriteCallback;

	public AbstractReadOnlyConverter(Mapper mapper, MappingContext<?> context, Class<T> entityClass) {
		_entityClass = entityClass;
		_constructor = ClassInformation.getConstructor(_entityClass);

		_logger.severe("Map " + _entityClass);

		Method onReadCallback = null;
		Method onWriteCallback = null;

		for (Method m : ClassInformation.getMethods(entityClass)) {
			m.setAccessible(true);
			if (m.getAnnotation(OnRead.class)!=null)
			{
				if (onReadCallback!=null) throw new MappingException(entityClass, "Only on @OnRead supported: "+m.getName()+" (allready on "+onReadCallback.getName()+")");
				onReadCallback=m;
			}
			if (m.getAnnotation(OnWrite.class)!=null)
			{
				if (onWriteCallback!=null) throw new MappingException(entityClass, "Only on @OnRead supported: "+m.getName()+" (allready on "+onWriteCallback.getName()+")");
				onWriteCallback=m;
			}
		}

		_onReadCallback = onReadCallback;
		_onWriteCallback = onWriteCallback;

	}

	protected Class<T> getEntityClass() {
		return _entityClass;
	}

	protected T newInstance() {
		try {
			return _constructor.newInstance();
		} catch (IllegalArgumentException e) {
			throw new MappingException(_entityClass, "newInstance", e);
		} catch (InstantiationException e) {
			throw new MappingException(_entityClass, "newInstance", e);
		} catch (IllegalAccessException e) {
			throw new MappingException(_entityClass, "newInstance", e);
		} catch (InvocationTargetException e) {
			throw new MappingException(_entityClass, "newInstance", e);
		}
	}
	
	protected void invokeOnReadCallback(T entity)
	{
		invokeCallback(entity, _onReadCallback);
	}

	protected void invokeOnWriteCallback(T entity)
	{
		invokeCallback(entity, _onWriteCallback);
	}

	private static <T> void invokeCallback(T ret, Method callback) {
		if (callback != null) {
			try {
				callback.invoke(ret, null);
			} catch (IllegalArgumentException e) {
				extractAndThrowException(e);
			} catch (IllegalAccessException e) {
				extractAndThrowException(e);
			} catch (InvocationTargetException e) {
				extractAndThrowException(e);
			}
		}
	}

	private static void extractAndThrowException(Exception e) {
		Throwable cause = e.getCause();
		if (cause instanceof ObjectMapperException)
			throw ((ObjectMapperException) cause);
		throw new ObjectMapperException(cause);
	}

	protected static <T> void setEntityField(T instance, MappedAttribute attribute, DBObject dbobject) {
		try {
			Field field = attribute.getField();
			String attributeName = attribute.getName();
			Object fieldValue = getValue(dbobject, attributeName);
			//				Object fieldValue = field.get(entity);
			if (fieldValue != null) {
				ITypeConverter converter = attribute.getConverter();
				field.set(instance, converter.convertFrom(fieldValue));
			}
		} catch (MappingException e) {
			throw new MappingException(instance.getClass(), e);
		} catch (IllegalArgumentException e) {
			throw new MappingException(instance.getClass(), e);
		} catch (IllegalAccessException e) {
			throw new MappingException(instance.getClass(), e);
		}
	}

	private static Object getValue(DBObject dbobject, String attributeName) {
		Object fieldValue = null;
		int dotIndex = attributeName.indexOf('.');
		if (dotIndex != -1) {
			String prefix = attributeName.substring(0, dotIndex);
			String left = attributeName.substring(dotIndex + 1);
			Object sub = dbobject.get(prefix);
			if (sub instanceof DBObject) {
				fieldValue = getValue((DBObject) sub, left);
			} else
				throw new IllegalArgumentException("Attribute " + prefix + " is not of type DBObject");
		} else {
			fieldValue = dbobject.get(attributeName);
		}
		return fieldValue;
	}

}
