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

package de.flapdoodle.mongoom.mapping;

import java.lang.reflect.Field;
import java.util.List;

import de.flapdoodle.mongoom.annotations.Transient;
import de.flapdoodle.mongoom.annotations.index.Indexed;
import de.flapdoodle.mongoom.annotations.index.IndexedInGroup;
import de.flapdoodle.mongoom.annotations.index.IndexedInGroups;
import de.flapdoodle.mongoom.datastore.index.IPropertyIndex;
import de.flapdoodle.mongoom.mapping.context.IMappingContext;
import de.flapdoodle.mongoom.mapping.context.IPropertyContext;
import de.flapdoodle.mongoom.mapping.converter.reflection.ClassInformation;
import de.flapdoodle.mongoom.mapping.naming.PropertyName;
import de.flapdoodle.mongoom.mapping.properties.IPropertyField;
import de.flapdoodle.mongoom.mapping.properties.Property;
import de.flapdoodle.mongoom.mapping.typeinfo.TypeInfo;

public abstract class AbstractClassFieldVisitor<Type, Mapped> extends AbstractVisitor {

	protected void parseProperties(IMappingContext mappingContext, IPropertyContext<?> rootContext, ITypeInfo typeInfo) {
		Class<Type> entityClass = (Class<Type>) typeInfo.getType();
		List<Field> fields = ClassInformation.getFields(entityClass);

		for (Field field : fields) {
			ITypeInfo fieldInfo = TypeInfo.of(typeInfo, field);

			IPropertyField<?> property = Property.of(mappingContext.naming().name(field, PropertyName.empty()), field);
			if (property.getField().getAnnotation(Transient.class) == null) {
				IPropertyContext propertyContext = rootContext.contextFor(property);

				IPropertyIndex propertyIndex = propertyContext.propertyIndex();

				Indexed indexed = property.getField().getAnnotation(Indexed.class);
				if (indexed != null)
					propertyIndex.setIndexed(indexed);
				IndexedInGroup indexedInGroup = property.getField().getAnnotation(IndexedInGroup.class);
				if (indexedInGroup != null) {
					propertyIndex.addIndexedInGroup(indexedInGroup);
				}
				IndexedInGroups indexedInGroups = property.getField().getAnnotation(IndexedInGroups.class);
				if ((indexedInGroups != null) && (indexedInGroups.value().length > 0)) {
					for (IndexedInGroup ig : indexedInGroups.value()) {
						propertyIndex.addIndexedInGroup(ig);
					}
				}

				ITypeVisitor typeVisitor = mappingContext.getVisitor(typeInfo, fieldInfo);
				if (typeVisitor == null)
					error(entityClass, "Could not get TypeVisitor for " + field);
				ITransformation transformation = typeVisitor.transformation(mappingContext, propertyContext, fieldInfo);
				if (transformation == null)
					error(entityClass, "Could not get Transformation for " + field);
				//			entityContext.addProperty(field.getName(),transformation);
				rootContext.setTransformation(property, transformation);
			}
		}
	}
}
