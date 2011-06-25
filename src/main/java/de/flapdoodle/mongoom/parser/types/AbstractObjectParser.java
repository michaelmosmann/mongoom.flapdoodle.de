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

package de.flapdoodle.mongoom.parser.types;

import java.lang.reflect.Field;
import java.util.List;

import de.flapdoodle.mongoom.annotations.Id;
import de.flapdoodle.mongoom.annotations.Transient;
import de.flapdoodle.mongoom.annotations.Version;
import de.flapdoodle.mongoom.annotations.index.Indexed;
import de.flapdoodle.mongoom.annotations.index.IndexedInGroup;
import de.flapdoodle.mongoom.annotations.index.IndexedInGroups;
import de.flapdoodle.mongoom.datastore.Indexes;
import de.flapdoodle.mongoom.mapping.converter.annotations.Annotations;
import de.flapdoodle.mongoom.mapping.converter.reflection.ClassInformation;
import de.flapdoodle.mongoom.mapping.index.IndexParser;
import de.flapdoodle.mongoom.mapping.index.OneOrOther;
import de.flapdoodle.mongoom.parser.FieldType;
import de.flapdoodle.mongoom.parser.IMappedProperty;
import de.flapdoodle.mongoom.parser.IMapProperties;
import de.flapdoodle.mongoom.parser.IType;
import de.flapdoodle.mongoom.parser.ITypeParser;
import de.flapdoodle.mongoom.parser.ITypeParserFactory;


public abstract class AbstractObjectParser<T extends IMapProperties> extends AbstractTypeParser {

	private final ITypeParserFactory _typeParserFactory;

	
	public AbstractObjectParser(ITypeParserFactory typeParserFactory) {
		_typeParserFactory=typeParserFactory;
	}

	protected void parseFields(T mapping, IType type) {
		
		Class<?> objectClass=type.getType();
		
		
		List<Field> fields = ClassInformation.getFields(objectClass);
		
		for (Field field : fields) {
			field.setAccessible(true);
			if (field.getAnnotation(Transient.class) == null) {
				checkAnnotations(objectClass, field);

				FieldType fieldType = FieldType.of(field);
				
				ITypeParser parser = _typeParserFactory.getParser(fieldType);
				if (parser==null) error(type,"no parser for "+field);
				
				IMappedProperty property = mapping.newProperty(fieldType, field.getName());
				parser.parse(property, fieldType);
				
				postProcessProperty(mapping,property);
			}
		}

	}

	protected void postProcessProperty(T mapping, IMappedProperty field) {
		OneOrOther<Indexed, IndexedInGroup[]> indexOrIdxInGroup = IndexParser.getIndexDef(mapping.getType().getType(), field.getType());
		if (indexOrIdxInGroup.getOne()!=null) field.setIndex(indexOrIdxInGroup.getOne());
		if (indexOrIdxInGroup.getOther()!=null) field.setIndexedInGroup(indexOrIdxInGroup.getOther());
		
	}

	protected void checkAnnotations(Class<?> objectClass, Field field) {
		Annotations.errorIfAnnotated(objectClass, field, Id.class, Version.class);
		Annotations.checkForOnlyOneAnnotation(objectClass, field, Indexed.class, IndexedInGroup.class,
				IndexedInGroups.class);
	}

}
