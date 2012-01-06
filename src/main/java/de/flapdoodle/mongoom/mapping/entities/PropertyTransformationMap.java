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

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;

import de.flapdoodle.mongoom.mapping.ITransformation;
import de.flapdoodle.mongoom.mapping.properties.IProperty;
import de.flapdoodle.mongoom.mapping.properties.PropertyName;
import de.flapdoodle.mongoom.mapping.properties.TypedPropertyName;


public class PropertyTransformationMap {
	
//	private final Map<TypedPropertyName<?>, ITransformation<?, ?>> propertyTransformation = Maps.newLinkedHashMap();
//	private final Map<String, ITransformation<?, ?>> propertynameTransformation = Maps.newLinkedHashMap();
//	private final Map<TypedPropertyName<?>, IProperty<?>> propertyMap = Maps.newLinkedHashMap();
	
	private final Map<PropertyName<?>, ITransformation<?, ?>> propertyNameTransformation = Maps.newLinkedHashMap();
	private final Map<String, PropertyName<?>> propertyNames = Maps.newLinkedHashMap();
	private final Map<TypedPropertyName<?>, PropertyName<?>> propertyTypeNames = Maps.newLinkedHashMap();
	private final Map<PropertyName<?>, IProperty<?>> propertyMap = Maps.newLinkedHashMap();
	
	public <S> void setTransformation(IProperty<S> property, ITransformation<S, ?> transformation) {
		TypedPropertyName<S> propertyName = TypedPropertyName.of(property.name().getName(), property.getType());
//		propertyTransformation.put(propertyName, transformation);
//		propertynameTransformation.put(property.name().getName(), transformation);
//		propertyMap.put(propertyName, property);
		
		propertyNameTransformation.put(property.name(), transformation);
		propertyNames.put(property.name().getName(), property.name());
		propertyTypeNames.put(propertyName, property.name());
		propertyMap.put(property.name(), property);
	}
	
	public IPropertyTransformations readOnly() {
		return new IPropertyTransformations() {

			@Override
			public <Source> ITransformation<Source, ?> get(PropertyName<Source> property) {
				return (ITransformation<Source, ?>) propertyNameTransformation.get(property);
			}
			
			@Override
			public PropertyName get(String property) {
				return propertyNames.get(property);
			}
			
			@Override
			public <Source> PropertyName get(TypedPropertyName<Source> property) {
				return propertyTypeNames.get(property);
			}
			
			@Override
			public <Source> IProperty<Source> getProperty(PropertyName<Source> p) {
				return (IProperty<Source>) propertyMap.get(p);
			}
			
			@Override
			public Set<PropertyName<?>> propertyNames() {
				return propertyMap.keySet();
			}
//			@Override
//			public <Source> ITransformation<Source, ?> get(TypedPropertyName<Source> property) {
//				return (ITransformation<Source, ?>) propertyTransformation.get(property);
//			}
//			
//			@Override
//			public ITransformation<?, ?> get(String property) {
//				return propertynameTransformation.get(property);
//			}
//
//			@Override
//			public <Source> IProperty<Source> getProperty(TypedPropertyName<Source> p) {
//				return (IProperty<Source>) propertyMap.get(p);
//			}
//
//			@Override
//			public Set<TypedPropertyName<?>> typedPropertyNames() {
//				return propertyMap.keySet();
//			}
			
			
			
		};
	}

}
