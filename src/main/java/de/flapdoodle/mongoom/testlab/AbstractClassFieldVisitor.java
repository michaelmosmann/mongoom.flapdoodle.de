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

package de.flapdoodle.mongoom.testlab;

import java.lang.reflect.Field;
import java.util.List;

import de.flapdoodle.mongoom.mapping.converter.reflection.ClassInformation;
import de.flapdoodle.mongoom.testlab.typeinfo.TypeInfo;

public abstract class AbstractClassFieldVisitor<Type, Mapped> extends AbstractVisitor {

	protected void parseProperties(IMappingContext mappingContext, IPropertyContext<?> rootContext, ITypeInfo typeInfo) {
		Class<Type> entityClass = (Class<Type>) typeInfo.getType();
		List<Field> fields = ClassInformation.getFields(entityClass);
		
		for (Field field : fields) {
			Property property = Property.of(field);
			IPropertyContext propertyContext = rootContext.contextFor(property);
			ITypeVisitor typeVisitor=mappingContext.getVisitor(typeInfo,TypeInfo.of(typeInfo,field));
			if (typeVisitor==null) error(entityClass,"Could not get TypeVisitor for "+field);
			ITransformation transformation = typeVisitor.transformation(mappingContext, propertyContext, TypeInfo.of(typeInfo,field));
			if (transformation==null) error(entityClass,"Could not get Transformation for "+field);
//			entityContext.addProperty(field.getName(),transformation);
			rootContext.setTransformation(property,transformation);
		}
	}
}
