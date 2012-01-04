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

import de.flapdoodle.mongoom.mapping.ITransformation;
import de.flapdoodle.mongoom.mapping.context.IPropertyContext;
import de.flapdoodle.mongoom.mapping.properties.IProperty;

public abstract class AbstractBeanContext<BeanType> implements IPropertyContext<BeanType>, IBeanContext<BeanType> {

	private final Class<BeanType> _viewClass;
//	private final Map<PropertyName<?>, ITransformation<?, ?>> propertyTransformation = Maps.newLinkedHashMap();
//	private final Map<PropertyName<?>, Property<?>> propertyMap = Maps.newLinkedHashMap();
	private final PropertyTransformationMap propertyTransformationMap = new PropertyTransformationMap();

	public AbstractBeanContext(Class<BeanType> viewClass) {
		_viewClass = viewClass;
	}

	public Class<BeanType> getViewClass() {
		return _viewClass;
	}

	@Override
	public <S> void setTransformation(IProperty<S> property, ITransformation<S, ?> transformation) {
//		PropertyName<S> propertyName = PropertyName.of(property.getName(), property.getType());
//		propertyTransformation.put(propertyName, transformation);
//		propertyMap.put(propertyName, property);
		
		propertyTransformationMap.setTransformation(property, transformation);
	}
	
	@Override
	public IPropertyTransformations getPropertyTransformations() {
		return propertyTransformationMap.readOnly();
	}

//	protected Map<PropertyName<?>, ITransformation<?, ?>> getPropertyTransformation() {
//		return Collections.unmodifiableMap(propertyTransformation);
//	}
//
//	protected Property<?> getProperty(PropertyName<?> name) {
//		return propertyMap.get(name);
//	}

}
