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

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;

import de.flapdoodle.mongoom.testlab.ITransformation;
import de.flapdoodle.mongoom.testlab.properties.Property;
import de.flapdoodle.mongoom.testlab.properties.PropertyName;


public class PropertyTransformationMap {
	
	private final Map<PropertyName<?>, ITransformation<?, ?>> propertyTransformation = Maps.newLinkedHashMap();
	private final Map<String, ITransformation<?, ?>> propertynameTransformation = Maps.newLinkedHashMap();
	private final Map<PropertyName<?>, Property<?>> propertyMap = Maps.newLinkedHashMap();
	
	public <S> void setTransformation(Property<S> property, ITransformation<S, ?> transformation) {
		PropertyName<S> propertyName = PropertyName.of(property.getName(), property.getType());
		propertyTransformation.put(propertyName, transformation);
		propertynameTransformation.put(property.getName(), transformation);
		propertyMap.put(propertyName, property);
	}
	
	public IPropertyTransformations readOnly() {
		return new IPropertyTransformations() {

			@Override
			public <Source> ITransformation<Source, ?> get(PropertyName<Source> property) {
				return (ITransformation<Source, ?>) propertyTransformation.get(property);
			}
			
			@Override
			public ITransformation<?, ?> get(String property) {
				return propertynameTransformation.get(property);
			}

			@Override
			public <Source> Property<Source> getProperty(PropertyName<Source> p) {
				return (Property<Source>) propertyMap.get(p);
			}

			@Override
			public Collection<PropertyName<?>> propertyNames() {
				return propertyMap.keySet();
			}
			
			
			
		};
	}

}
