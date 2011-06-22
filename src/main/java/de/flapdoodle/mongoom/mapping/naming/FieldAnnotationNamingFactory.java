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

package de.flapdoodle.mongoom.mapping.naming;

import java.lang.reflect.Field;

import de.flapdoodle.mongoom.annotations.Id;
import de.flapdoodle.mongoom.annotations.Property;
import de.flapdoodle.mongoom.annotations.Version;
import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.mapping.Const;
import de.flapdoodle.mongoom.mapping.IFieldNamingFactory;

public class FieldAnnotationNamingFactory implements IFieldNamingFactory {

	@Override
	public String getFieldName(Field field) {
		Property propertyAnnotation = field.getAnnotation(Property.class);
		Id idAnnotation = field.getAnnotation(Id.class);
		Version versionAnnotation = field.getAnnotation(Version.class);

		if ((propertyAnnotation != null) && (idAnnotation != null)) {
			throw new MappingException(field.getDeclaringClass(), "Id and Property in same place: " + field);
		}
		if ((versionAnnotation != null) && (idAnnotation != null)) {
			throw new MappingException(field.getDeclaringClass(), "Id and Version in same place: " + field);
		}
		if (idAnnotation != null) {
			return Const.ID_FIELDNAME;
		}
		if (propertyAnnotation != null) {
			return propertyAnnotation.value();
		}
		if (versionAnnotation != null) {
			return Const.VERSION_FIELDNAME;
		}
		return null;
	}
}
