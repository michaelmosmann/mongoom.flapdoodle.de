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

public class FieldAnnotationNaming implements IPropertyNaming {

	@Override
	public PropertyName name(Field fieldType, PropertyName current) {
		Property propertyAnnotation = fieldType.getAnnotation(Property.class);
		Id idAnnotation = fieldType.getAnnotation(Id.class);
		Version versionAnnotation = fieldType.getAnnotation(Version.class);

		if ((propertyAnnotation != null) && (idAnnotation != null)) {
			throw new MappingException(fieldType.getType(), "Id and Property in same place: " + fieldType);
		}
		if ((versionAnnotation != null) && (idAnnotation != null)) {
			throw new MappingException(fieldType.getType(), "Id and Version in same place: " + fieldType);
		}
		if (idAnnotation != null) {
			return PropertyName.with(Const.ID_FIELDNAME,current.getType());
		}
		if (versionAnnotation != null) {
			return PropertyName.with(Const.VERSION_FIELDNAME,current.getType());
		}
		if (propertyAnnotation != null) {
			return PropertyName.with(current.getName(),propertyAnnotation.value(),current.getType());
		}
		return current;
	}
}
