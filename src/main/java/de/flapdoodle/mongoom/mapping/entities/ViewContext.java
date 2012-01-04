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

import de.flapdoodle.mongoom.annotations.index.Indexed;
import de.flapdoodle.mongoom.annotations.index.IndexedInGroup;
import de.flapdoodle.mongoom.datastore.index.IPropertyIndex;
import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.mapping.context.IPropertyContext;
import de.flapdoodle.mongoom.mapping.context.PropertyContext;
import de.flapdoodle.mongoom.mapping.properties.IProperty;
import de.flapdoodle.mongoom.mapping.properties.IPropertyName;


public class ViewContext<ViewBean> extends AbstractBeanContext<ViewBean> implements IPropertyContext<ViewBean> {

	public ViewContext(Class<ViewBean> viewClass) {
		super(viewClass);
	}

	@Override
	public <S> IPropertyContext<S> contextFor(IProperty<S> of) {
		return new PropertyContext<S>(this,of);
	}

	@Override
	public IPropertyIndex propertyIndex() {
		throw new MappingException(getViewClass(),"not allowed");
	}

	@Override
	public void addIndexedInGroup(IPropertyName name, IndexedInGroup ig) {
		throw new MappingException(getViewClass(),"not allowed");		
	}

	@Override
	public void setIndexed(IPropertyName name, Indexed ig) {
		throw new MappingException(getViewClass(),"not allowed");		
	}
	
//	protected Map<PropertyName<?>, ITransformation<?, ?>> getPropertyTransformation() {
//		return Collections.unmodifiableMap(propertyTransformation);
//	}
//
//	protected Property<?> getProperty(PropertyName<?> name) {
//		return propertyMap.get(name);
//	}
}