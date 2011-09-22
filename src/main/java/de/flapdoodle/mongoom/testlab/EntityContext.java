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

import java.util.Map;

import com.google.common.collect.Maps;

import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.annotations.Views;
import de.flapdoodle.mongoom.mapping.index.EntityIndexDef;


public class EntityContext<EntityBean> implements IEntityContext{

	private final Class<EntityBean> _entityClass;
	private final Entity _entityAnnotation;
	private final Views _viewsAnnotation;
	private final Map<String, EntityIndexDef> _indexGroupMap;
	
	private final Map<Property, PropertyContext<EntityBean, ?>> _properties=Maps.newLinkedHashMap();

	public EntityContext(Class<EntityBean> entityClass,Entity entityAnnotation, Views viewsAnnotation, Map<String, EntityIndexDef> indexGroupMap) {
		_entityClass = entityClass;
		_entityAnnotation = entityAnnotation;
		_viewsAnnotation = viewsAnnotation;
		_indexGroupMap = indexGroupMap;
		
	}
	
	@Override
	public IPropertyContext contextFor(Property property) {
		PropertyContext ret = new PropertyContext(this);
		_properties.put(property, ret);
		return ret;
	}

	static class PropertyContext<EntityBean,T> implements IPropertyContext<T> {
		
		private final EntityContext<EntityBean> _entityContext;
		private final Map<Property, PropertyContext<EntityBean, ?>> _properties=Maps.newLinkedHashMap();
		private final PropertyContext<EntityBean, ?> _parent;

		public PropertyContext(EntityContext<EntityBean> entityContext) {
			_entityContext = entityContext;
			_parent=null;
		}

		public PropertyContext(EntityContext<EntityBean> entityContext, PropertyContext<EntityBean, ?> propertyContext) {
			_entityContext = entityContext;
			_parent = propertyContext;
		}

		@Override
		public <S> IPropertyContext<S> contextFor(Property<S> property) {
			PropertyContext<EntityBean, S> ret = new PropertyContext<EntityBean, S>(_entityContext,this);
			_properties.put(property,ret);
			return ret;
		}
	}
}
