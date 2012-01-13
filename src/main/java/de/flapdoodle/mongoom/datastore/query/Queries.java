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

package de.flapdoodle.mongoom.datastore.query;

import java.util.Arrays;

import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.mapping.ITransformation;
import de.flapdoodle.mongoom.mapping.naming.PropertyName;
import de.flapdoodle.mongoom.mapping.properties.IPropertyMappedName;
import de.flapdoodle.mongoom.mapping.properties.Property;
import de.flapdoodle.mongoom.mapping.properties.TypedPropertyName;


public class Queries {
	private Queries() {
		throw new IllegalAccessError("Singleton");
	}
	
	protected static <T> MappedNameTransformation getConverter(TypedPropertyName<T> field, ITransformation converter, IPropertyMappedName name) {
		ITransformation lastConverter=converter;
		PropertyName propertyName = converter.propertyName(field);
		if (name == null)
			name = propertyName;
		else {
			name = Property.append(name, propertyName);
		}
		converter = converter.propertyTransformation(propertyName);
		if (converter == null)
			throw new MappingException("No Converter for " + Arrays.asList(field) + " in " + lastConverter);
		return new MappedNameTransformation(name, converter);
	}
	

}
