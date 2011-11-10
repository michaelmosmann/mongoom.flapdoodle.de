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

import de.flapdoodle.mongoom.testlab.ITransformation;
import de.flapdoodle.mongoom.testlab.properties.Property;
import de.flapdoodle.mongoom.testlab.properties.PropertyName;


public interface IPropertyTransformations {
	<Source> ITransformation<Source, ?> get(PropertyName<Source> property);
	ITransformation<?, ?> get(String property);
	
	<Source> Property<Source> getProperty(PropertyName<Source> p);
	
	Collection<PropertyName<?>> propertyNames();
}
