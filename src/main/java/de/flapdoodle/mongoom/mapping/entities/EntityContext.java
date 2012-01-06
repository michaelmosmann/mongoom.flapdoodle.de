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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.DBObject;

import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.annotations.Views;
import de.flapdoodle.mongoom.annotations.index.IndexOption;
import de.flapdoodle.mongoom.annotations.index.Indexed;
import de.flapdoodle.mongoom.annotations.index.IndexedInGroup;
import de.flapdoodle.mongoom.datastore.index.IIndex;
import de.flapdoodle.mongoom.datastore.index.IPropertyIndex;
import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.mapping.IEntityContext;
import de.flapdoodle.mongoom.mapping.ITransformation;
import de.flapdoodle.mongoom.mapping.IViewTransformation;
import de.flapdoodle.mongoom.mapping.callbacks.IEntityReadCallback;
import de.flapdoodle.mongoom.mapping.callbacks.IEntityWriteCallback;
import de.flapdoodle.mongoom.mapping.context.IPropertyContext;
import de.flapdoodle.mongoom.mapping.context.PropertyContext;
import de.flapdoodle.mongoom.mapping.index.EntityIndexDef;
import de.flapdoodle.mongoom.mapping.index.FieldIndex;
import de.flapdoodle.mongoom.mapping.index.IndexDef;
import de.flapdoodle.mongoom.mapping.properties.IProperty;
import de.flapdoodle.mongoom.mapping.properties.IPropertyField;
import de.flapdoodle.mongoom.mapping.properties.IPropertyMappedName;
import de.flapdoodle.mongoom.mapping.properties.IPropertyName;
import de.flapdoodle.mongoom.mapping.properties.PropertyName;
import de.flapdoodle.mongoom.mapping.versions.IVersionFactory;

public class EntityContext<EntityBean> extends AbstractBeanContext<EntityBean> implements IEntityContext<EntityBean>, IBeanContext<EntityBean> {

	private final Entity _entityAnnotation;
	private final Views _viewsAnnotation;
	private final Map<String, EntityIndexDef> _indexGroupMap;
	private final Map<String, IndexDef> _indexDef;
	
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
		_indexDef = Maps.newLinkedHashMap();
	}

	@Override
	public <S> IPropertyContext<S> contextFor(IProperty<S> of) {
		return new PropertyContext<S>(this,of);
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
		IViewTransformation<?, DBObject> ret = _viewTransformation.get(viewType);
		if (ret==null) throw new MappingException(getEntityClass(),"ViewTransformation for "+viewType);
		return (IViewTransformation<Source, DBObject>) ret;
	}

	protected <Source> void setViewTransformation(Class<Source> viewType,
			IViewTransformation<Source, DBObject> transformation) {
		if (transformation.properties()==null) throw new MappingException(getEntityClass(),"View has no properties: "+viewType);
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
		return new Index();
	}

	@Override
	public IPropertyIndex propertyIndex() {
		throw new MappingException(getViewClass(),"should not be called");
	}
	
	@Override
	public void addIndexedInGroup(IPropertyMappedName name, IndexedInGroup ig) {
		EntityIndexDef entityIndexDef = _indexGroupMap.get(ig.group());
		if (entityIndexDef==null) {
			throw new MappingException(getEntityClass(),"IndexGroup not found:"+ig);		
		}
		entityIndexDef.addField(new FieldIndex(name.getMapped(), ig.direction(), ig.priority()));
	}
	
	@Override
	public void setIndexed(IPropertyMappedName name, Indexed ig) {
		IndexOption options = ig.options();
		String propName = name.getMapped();
		_indexDef.put(propName,new IndexDef(propName, Lists.newArrayList(new FieldIndex(propName, ig.direction(), 0)), options.unique(), options.dropDups(), options.sparse()));
	}
	
	class Index implements IIndex {

		@Override
		public List<IndexDef> list() {
			ArrayList<IndexDef> ret = Lists.newArrayList();
			ret.addAll(_indexDef.values());
			ret.addAll(_indexGroupMap.values());
			return ret;
		}
		
	}
}
