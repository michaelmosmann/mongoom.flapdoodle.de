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

import java.util.Map;

import com.google.common.collect.Maps;
import com.mongodb.DBObject;

import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.annotations.Views;
import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.mapping.callbacks.IEntityReadCallback;
import de.flapdoodle.mongoom.mapping.callbacks.IEntityWriteCallback;
import de.flapdoodle.mongoom.mapping.index.EntityIndexDef;
import de.flapdoodle.mongoom.testlab.IEntityContext;
import de.flapdoodle.mongoom.testlab.ITransformation;
import de.flapdoodle.mongoom.testlab.IViewTransformation;
import de.flapdoodle.mongoom.testlab.datastore.index.IIndex;
import de.flapdoodle.mongoom.testlab.mapping.IPropertyContext;
import de.flapdoodle.mongoom.testlab.mapping.PropertyContext;
import de.flapdoodle.mongoom.testlab.properties.IProperty;
import de.flapdoodle.mongoom.testlab.properties.IPropertyField;
import de.flapdoodle.mongoom.testlab.properties.Property;
import de.flapdoodle.mongoom.testlab.versions.IVersionFactory;

public class EntityContext<EntityBean> extends AbstractBeanContext<EntityBean> implements IEntityContext<EntityBean> {

	private final Entity _entityAnnotation;
	private final Views _viewsAnnotation;
	private final Map<String, EntityIndexDef> _indexGroupMap;
	private final Map<Class<?>, IViewTransformation<?, DBObject>> _viewTransformation = Maps.newHashMap();

	private IPropertyField<?> _versionProperty;
	private IVersionFactory<?> _versionFactory;
	private IPropertyField<?> _idProperty;
	private ITransformation<?, ?> _idTransformation;

	private IEntityWriteCallback<EntityBean> _writeCallback;
	private IEntityReadCallback<EntityBean> _readCallback;

	public EntityContext(Class<EntityBean> entityClass, Entity entityAnnotation, Views viewsAnnotation,
			Map<String, EntityIndexDef> indexGroupMap) {
		super(entityClass);
		_entityAnnotation = entityAnnotation;
		_viewsAnnotation = viewsAnnotation;
		_indexGroupMap = indexGroupMap;

	}

	@Override
	public <S> IPropertyContext<S> contextFor(IProperty<S> of) {
		return new PropertyContext<S>(this);
	}

	public Class<EntityBean> getEntityClass() {
		return super.getViewClass();
	}

	public Entity getEntityAnnotation() {
		return _entityAnnotation;
	}

	//	@Override
	//	public <S> void setTransformation(Property<S> property, ITransformation<S, ?> transformation) {
	//		PropertyName<S> propertyName = PropertyName.of(property.getName(),property.getType());
	//		propertyTransformation.put(propertyName, transformation);
	//		propertyMap.put(propertyName, property);
	//	}

	//	protected Map<PropertyName<?>, ITransformation<?, ?>> getPropertyTransformation() {
	//		return Collections.unmodifiableMap(propertyTransformation);
	//	}

	//	protected Property<?> getProperty(PropertyName<?> name) {
	//		return propertyMap.get(name);
	//	}

	@Override
	public void setVersionFactory(IPropertyField<?> props, IVersionFactory<?> versionFactory) {
		_versionProperty = props;
		_versionFactory = versionFactory;
	}

	public IPropertyField<?> getVersionProperty() {
		return _versionProperty;
	}

	public IVersionFactory<?> getVersionFactory() {
		return _versionFactory;
	}

	public <Source> IViewTransformation<Source, DBObject> viewTransformation(Class<Source> viewType) {
		return (IViewTransformation<Source, DBObject>) _viewTransformation.get(viewType);
	}

	protected <Source> void setViewTransformation(Class<Source> viewType,
			IViewTransformation<Source, DBObject> transformation) {
		_viewTransformation.put(viewType, transformation);
	}

	public void setId(IPropertyField<?> prop, ITransformation<?, ?> transformation) {
		if (_idProperty != null)
			throw new MappingException(getEntityClass(), "Id allready set");
		_idProperty = prop;
		_idTransformation = transformation;
	}

	public IPropertyField<?> getIdProperty() {
		return _idProperty;
	}

	public ITransformation<?, ?> getIdTransformation() {
		return _idTransformation;
	}

	public void setWriteCallback(IEntityWriteCallback<EntityBean> writeCallback) {
		_writeCallback = writeCallback;
	}

	public void setReadCallback(IEntityReadCallback<EntityBean> readCallback) {
		_readCallback = readCallback;
	}

	public IEntityWriteCallback<EntityBean> getWriteCallback() {
		return _writeCallback;
	}

	public IEntityReadCallback<EntityBean> getReadCallback() {
		return _readCallback;
	}

	public IIndex index() {
		return null;
	}
}
