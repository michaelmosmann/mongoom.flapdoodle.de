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

package de.flapdoodle.mongoom.testlab.entities;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.testlab.ITransformation;
import de.flapdoodle.mongoom.testlab.properties.IProperty;
import de.flapdoodle.mongoom.testlab.properties.IPropertyField;
import de.flapdoodle.mongoom.testlab.properties.Property;
import de.flapdoodle.mongoom.testlab.properties.TypedPropertyName;

public abstract class AbstractBeanTransformation<Bean, C extends AbstractBeanContext<Bean>> implements
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

		for (TypedPropertyName p : propertyTransformations.typedPropertyNames()) {
			ITransformation transformation = propertyTransformations.get(p);
			IPropertyField<?> prop = (IPropertyField<?>) propertyTransformations.getProperty(p);
			Field field = prop.getField();
			Object fieldValue = getFieldValue(field, value);
			Object dbValue = transformation.asObject(fieldValue);
			if (dbValue != null)
				ret.put(p.getName(), dbValue);
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

		for (TypedPropertyName p : propertyTransformations.typedPropertyNames()) {
			ITransformation transformation = propertyTransformations.get(p);
			IPropertyField<?> prop = (IPropertyField<?>) propertyTransformations.getProperty(p);
			Field field = prop.getField();
			Object fieldValue = transformation.asEntity(getValue(object, p));
			if (fieldValue != null)
				setFieldValue(ret, field, fieldValue);
		}

		return ret;
	}

	private Object getValue(DBObject object, TypedPropertyName p) {
		String[] path = p.getName().split("\\.");
		return getValue(object, Arrays.asList(path));
	}

	private Object getValue(DBObject object, List<String> path) {
		if (path.size() > 1) {
			Object property = object.get(path.get(0));
			if (property instanceof DBObject)
				return getValue((DBObject) property, path.subList(1, path.size()));
			else
				throw new MappingException(_entityContext.getViewClass(), "Property not found");
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
			return _entityContext.getViewClass().newInstance();
		} catch (InstantiationException e) {
			throw new MappingException(_entityContext.getViewClass(), e);
		} catch (IllegalAccessException e) {
			throw new MappingException(_entityContext.getViewClass(), e);
		}
	}

	@Override
	public <Source> ITransformation<Source, ?> propertyTransformation(TypedPropertyName<Source> property) {
		return (ITransformation<Source, ?>) _entityContext.getPropertyTransformations().get(property);
	}

	@Override
	public ITransformation<?, ?> propertyTransformation(String property) {
		return (ITransformation<?, ?>) _entityContext.getPropertyTransformations().get(property);
	}
	
	@Override
	public Set<TypedPropertyName<?>> properties() {
		return null;
	}

}
