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

package de.flapdoodle.mongoom.mapping.context;

import de.flapdoodle.mongoom.annotations.index.Indexed;
import de.flapdoodle.mongoom.annotations.index.IndexedInGroup;
import de.flapdoodle.mongoom.datastore.index.IPropertyIndex;
import de.flapdoodle.mongoom.mapping.ITransformation;
import de.flapdoodle.mongoom.mapping.naming.PropertyName;
import de.flapdoodle.mongoom.mapping.properties.IProperty;
import de.flapdoodle.mongoom.mapping.properties.IPropertyMappedName;
import de.flapdoodle.mongoom.mapping.properties.IPropertyName;


public interface IPropertyContext<T> {

	<S> IPropertyContext<S> contextFor(IProperty<S> of);

	<S> void setTransformation(IProperty<S> property, ITransformation<S,?> transformation);

	IPropertyIndex propertyIndex();

	void addIndexedInGroup(IPropertyMappedName name, IndexedInGroup ig);

	void setIndexed(IPropertyMappedName name, Indexed ig);
}
