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

package de.flapdoodle.mongoom.testlab;

import java.util.Collections;
import java.util.Map;

import org.omg.CORBA._PolicyStub;

import com.google.common.collect.Maps;

import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.annotations.Views;
import de.flapdoodle.mongoom.mapping.index.EntityIndexDef;

public class EntityContext<EntityBean> implements IEntityContext<EntityBean> {

	private final Class<EntityBean> _entityClass;
	private final Entity _entityAnnotation;
	private final Views _viewsAnnotation;
	private final Map<String, EntityIndexDef> _indexGroupMap;

	private final Map<Property<?>, ITransformation<?, ?>> propertyTransformation = Maps.newLinkedHashMap();

	public EntityContext(Class<EntityBean> entityClass, Entity entityAnnotation, Views viewsAnnotation,
			Map<String, EntityIndexDef> indexGroupMap) {
		_entityClass = entityClass;
		_entityAnnotation = entityAnnotation;
		_viewsAnnotation = viewsAnnotation;
		_indexGroupMap = indexGroupMap;

	}

	@Override
	public <S> IPropertyContext<S> contextFor(Property<S> of) {
		//		EntityPropertyContext ret = new EntityPropertyContext(this,property);
		//		return ret;
		return null;
	}
	
	
	public Class<EntityBean> getEntityClass() {
		return _entityClass;
	}

	@Override
	public <S> void setTransformation(Property<S> property, ITransformation<S, ?> transformation) {
		propertyTransformation.put(property, transformation);
	}
	
	
	protected Map<Property<?>, ITransformation<?, ?>> getPropertyTransformation() {
		return Collections.unmodifiableMap(propertyTransformation);
	}

	//	@Override
	//	public IPropertyContext contextFor(Property property) {
	//		EntityPropertyContext ret = new EntityPropertyContext(this,property);
	//		return ret;
	//	}

	//	
	//	static class EntityPropertyContext<EntityBean,T> implements IPropertyContext<T> {
	//		
	//		private final EntityContext<EntityBean> _entityContext;
	//		private final Property _property;
	//		
	//		public EntityPropertyContext(EntityContext<EntityBean> entityContext, Property property) {
	//			_entityContext = entityContext;
	//			_property = property;
	//		}
	//
	//		@Override
	//		public <S> IPropertyContext<S> contextFor(Property<S> property) {
	//			PropertyContext<EntityBean, S> ret = new PropertyContext<EntityBean, S>(_entityContext,this,property);
	//			return ret;
	//		}
	//		
	//		@Override
	//		public void setTransformation(ITransformation<T, ?> transformation) {
	//			_entityContext.setTransformation(_property,transformation);
	//		}
	//	}
	//
	//	static class PropertyContext<EntityBean,T> implements IPropertyContext<T> {
	//		
	//		private final EntityContext<EntityBean> _entityContext;
	//		private final IPropertyContext<?> _parent;
	//
	//		public PropertyContext(EntityContext<EntityBean> entityContext,
	//				IPropertyContext<?> propertyContext, Property<T> property) {
	//			_entityContext = entityContext;
	//			_parent = propertyContext;
	//		}
	//
	//		@Override
	//		public <S> IPropertyContext<S> contextFor(Property<S> property) {
	//			PropertyContext<EntityBean, S> ret = new PropertyContext<EntityBean, S>(_entityContext,this,property);
	//			return ret;
	//		}
	//		
	//		@Override
	//		public void setTransformation(ITransformation<T, ?> transformation) {
	////			if (_)
	//		}
	//	}
	//
	//	public void setTransformation(Property property, ITransformation<?, ?> transformation) {
	//		propertyTransformation.put(property, transformation);
	//	}
}
