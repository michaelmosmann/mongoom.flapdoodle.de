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

package de.flapdoodle.mongoom.testlab.mapping;

import java.util.Collections;
import java.util.Map;

import com.google.common.collect.Maps;

import de.flapdoodle.mongoom.annotations.index.Indexed;
import de.flapdoodle.mongoom.annotations.index.IndexedInGroup;
import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.testlab.ITransformation;
import de.flapdoodle.mongoom.testlab.datastore.index.IPropertyIndex;
import de.flapdoodle.mongoom.testlab.properties.IProperty;
import de.flapdoodle.mongoom.testlab.properties.IPropertyName;
import de.flapdoodle.mongoom.testlab.properties.Property;


public class PropertyContext<T> implements IPropertyContext<T>{

	private final Map<IProperty<?>, ITransformation<?, ?>> propertyTransformation = Maps.newLinkedHashMap();
	private final IPropertyContext<?> _parentContext;
	private final IProperty<T> _property;

	public PropertyContext(IPropertyContext<?> parentContext,IProperty<T> property) {
		_parentContext=parentContext;
		_property = property;
	}
	
	public PropertyContext(IPropertyContext<?> parentContext) {
		this(parentContext,null);
	}

	@Override
	public <S> IPropertyContext<S> contextFor(IProperty<S> of) {
		throw new MappingException("should never happen");
	}

	@Override
	public <S> void setTransformation(IProperty<S> property, ITransformation<S, ?> transformation) {
		propertyTransformation.put(property, transformation);
	}
	
	public Map<IProperty<?>, ITransformation<?, ?>> getPropertyTransformation() {
		return Collections.unmodifiableMap(propertyTransformation);
	}

	@Override
	public IPropertyIndex propertyIndex() {
		return new Index();
	}
	
	@Override
	public void addIndexedInGroup(IPropertyName name, IndexedInGroup ig) {
		_parentContext.addIndexedInGroup(Property.append(_property, name), ig);
	}
	
	@Override
	public void setIndexed(IPropertyName name, Indexed ig) {
		_parentContext.setIndexed(Property.append(_property, name), ig);
	}
	
	class Index implements IPropertyIndex {

		@Override
		public void addIndexedInGroup(IndexedInGroup ig) {
			_parentContext.addIndexedInGroup(_property, ig);
		}

		@Override
		public void setIndexed(Indexed ig) {
			_parentContext.setIndexed(_property, ig);			
		}
		
	}
}
