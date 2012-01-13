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

import java.util.Set;

import de.flapdoodle.mongoom.mapping.ITransformation;
import de.flapdoodle.mongoom.mapping.naming.PropertyName;
import de.flapdoodle.mongoom.mapping.properties.IProperty;
import de.flapdoodle.mongoom.mapping.properties.PropertyReference;


public interface IPropertyTransformations {
	<Source> PropertyName<Source> get(PropertyReference<Source> property);
	PropertyName<?> get(String property);
	<Source> ITransformation<Source, ?> get(PropertyName<Source> property);
	
	<Source> IProperty<Source> getProperty(PropertyName<Source> p);
	
	Set<PropertyName<?>> propertyNames();

	//	<Source> ITransformation<Source, ?> get(TypedPropertyName<Source> property);
//	ITransformation<?, ?> get(String property);
//	
//	<Source> IProperty<Source> getProperty(TypedPropertyName<Source> p);
//	
//	Set<TypedPropertyName<?>> typedPropertyNames();
}
