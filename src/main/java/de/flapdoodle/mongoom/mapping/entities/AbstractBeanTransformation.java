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

package de.flapdoodle.mongoom.mapping.entities;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.mapping.ITransformation;
import de.flapdoodle.mongoom.mapping.converter.reflection.ClassInformation;
import de.flapdoodle.mongoom.mapping.naming.PropertyName;
import de.flapdoodle.mongoom.mapping.properties.IPropertyField;
import de.flapdoodle.mongoom.mapping.properties.Property;
import de.flapdoodle.mongoom.mapping.properties.TypedPropertyName;

public abstract class AbstractBeanTransformation<Bean, C extends IBeanContext<Bean>> implements
		ITransformation<Bean, DBObject> {

	protected final C _entityContext;

	protected AbstractBeanTransformation(C context) {
		_entityContext = context;
	}

	protected C getContext() {
		return _entityContext;
	}

	@Override
	public DBObject asObject(Bean value) {
		if (value == null)
			return null;

		BasicDBObject ret = new BasicDBObject();
		IPropertyTransformations propertyTransformations = _entityContext.getPropertyTransformations();

		for (PropertyName p : propertyTransformations.propertyNames()) {
			ITransformation transformation = propertyTransformations.get(p);
			IPropertyField<?> prop = (IPropertyField<?>) propertyTransformations.getProperty(p);
			Field field = prop.getField();
			Object fieldValue = getFieldValue(field, value);
			Object dbValue = transformation.asObject(fieldValue);
			if (dbValue != null)
				ret.put(p.getMapped(), dbValue);
		}
		return ret;
	}

	protected Object getFieldValue(Field field, Bean value) {
		try {
			field.setAccessible(true);
			return field.get(value);
		} catch (IllegalArgumentException e) {
			throw new MappingException(_entityContext.getViewClass(), e);
		} catch (IllegalAccessException e) {
			throw new MappingException(_entityContext.getViewClass(), e);
		}
	}

	@Override
	public Bean asEntity(DBObject object) {
		if (object == null)
			return null;

		Bean ret = newInstance();
		IPropertyTransformations propertyTransformations = _entityContext.getPropertyTransformations();

		for (PropertyName p : propertyTransformations.propertyNames()) {
			ITransformation transformation = propertyTransformations.get(p);
			IPropertyField<?> prop = (IPropertyField<?>) propertyTransformations.getProperty(p);
			Field field = prop.getField();
			Object fieldValue = transformation.asEntity(getValue(object, p));
			if (fieldValue != null)
				setFieldValue(ret, field, fieldValue);
		}

		return ret;
	}

	private Object getValue(DBObject object, PropertyName p) {
		return getValue(object, Property.split(p.getMapped()));
	}

	private Object getValue(DBObject object, List<String> path) {
		if (path.size() > 1) {
			Object property = object.get(path.get(0));
			if (property instanceof DBObject)
				return getValue((DBObject) property, path.subList(1, path.size()));
			else
				// property is null?
				return null;
//				throw new MappingException(_entityContext.getViewClass(), "Property "+path+" not found "+object);
		}
		return object.get(path.get(0));
	}

	protected void setFieldValue(Bean bean, Field field, Object fieldValue) {
		try {
			field.setAccessible(true);
			field.set(bean, fieldValue);
		} catch (IllegalArgumentException e) {
			throw new MappingException(_entityContext.getViewClass(), e);
		} catch (IllegalAccessException e) {
			throw new MappingException(_entityContext.getViewClass(), e);
		}
	}

	private Bean newInstance() {
		try {
			Class<Bean> viewClass = _entityContext.getViewClass();
			Constructor<Bean> defaultConstrutor = ClassInformation.getConstructor(viewClass);
//			Constructor<?>[] constructors = viewClass.getConstructors();
//			for (Constructor c : constructors) {
//				System.out.println("Const: "+c);
//			}
//			Constructor<Bean> defaultConstrutor = viewClass.getConstructor();
//			return viewClass.newInstance();
			return defaultConstrutor.newInstance();
		} catch (InstantiationException e) {
			throw new MappingException(_entityContext.getViewClass(), e);
		} catch (IllegalAccessException e) {
			throw new MappingException(_entityContext.getViewClass(), e);
		} catch (SecurityException e) {
			throw new MappingException(_entityContext.getViewClass(), e);
		} catch (IllegalArgumentException e) {
			throw new MappingException(_entityContext.getViewClass(), e);
		} catch (InvocationTargetException e) {
			throw new MappingException(_entityContext.getViewClass(), e);
		}
	}

	@Override
	public <Source> PropertyName<Source> propertyName(TypedPropertyName<Source> property) {
		// TODO Auto-generated method stub
		return _entityContext.getPropertyTransformations().get(property);
	}
	
	@Override
	public PropertyName<?> propertyName(String property) {
		return _entityContext.getPropertyTransformations().get(property);
	}
	
	@Override
	public <Source> ITransformation<Source, ?> propertyTransformation(PropertyName<Source> property) {
		return _entityContext.getPropertyTransformations().get(property);
	}
//	@Override
//	public <Source> ITransformation<Source, ?> propertyTransformation(TypedPropertyName<Source> property) {
//		return (ITransformation<Source, ?>) _entityContext.getPropertyTransformations().get(property);
//	}
//
//	@Override
//	public ITransformation<?, ?> propertyTransformation(String property) {
//		return (ITransformation<?, ?>) _entityContext.getPropertyTransformations().get(property);
//	}

	@Override
	public Set<PropertyName<?>> properties() {
		return _entityContext.getPropertyTransformations().propertyNames();
	}
	
//	@Override
//	public Set<TypedPropertyName<?>> properties() {
//		return _entityContext.getPropertyTransformations().typedPropertyNames();
//	}

}
