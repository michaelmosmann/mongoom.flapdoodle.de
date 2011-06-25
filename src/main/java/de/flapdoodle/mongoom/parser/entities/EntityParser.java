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

package de.flapdoodle.mongoom.parser.entities;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.annotations.Id;
import de.flapdoodle.mongoom.annotations.Transient;
import de.flapdoodle.mongoom.annotations.Version;
import de.flapdoodle.mongoom.annotations.index.Indexed;
import de.flapdoodle.mongoom.annotations.index.IndexedInGroup;
import de.flapdoodle.mongoom.annotations.index.IndexedInGroups;
import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.mapping.converter.annotations.Annotations;
import de.flapdoodle.mongoom.mapping.converter.reflection.ClassInformation;
import de.flapdoodle.mongoom.mapping.index.EntityIndexDef;
import de.flapdoodle.mongoom.mapping.index.IndexParser;
import de.flapdoodle.mongoom.parser.AbstractParser;
import de.flapdoodle.mongoom.parser.ClassType;
import de.flapdoodle.mongoom.parser.FieldType;
import de.flapdoodle.mongoom.parser.IEntityMapping;
import de.flapdoodle.mongoom.parser.IEntityParser;
import de.flapdoodle.mongoom.parser.IMappedProperty;
import de.flapdoodle.mongoom.parser.IMapping;
import de.flapdoodle.mongoom.parser.IMapProperties;
import de.flapdoodle.mongoom.parser.IType;
import de.flapdoodle.mongoom.parser.ITypeParser;
import de.flapdoodle.mongoom.parser.ITypeParserFactory;
import de.flapdoodle.mongoom.parser.mapping.EntityMapping;
import de.flapdoodle.mongoom.parser.mapping.Mapping;
import de.flapdoodle.mongoom.parser.types.AbstractObjectParser;

public class EntityParser extends AbstractObjectParser<IEntityMapping> implements IEntityParser {

	public EntityParser(ITypeParserFactory typeParserFactory) {
		super(typeParserFactory);
	}

	@Override
	public void parse(IMapping mapping, IType type) {
		Class<?> entityClass = type.getType();

		Entity entityAnnotation = entityClass.getAnnotation(Entity.class);
		if (entityAnnotation == null) {
			error(type, "Missing " + Entity.class + " Annotation");
		}
		Map<String, EntityIndexDef> indexGroupMap = IndexParser.getIndexGroupMap(entityClass);

		IEntityMapping entityMapping = mapping.newEntity(ClassType.of(entityClass));

		entityMapping.setIndexGroups(indexGroupMap);
		
		parseFields(entityMapping, type);
	}

	@Override
	protected void postProcessProperty(IEntityMapping mapping, IMappedProperty fieldMapping) {
		IType fieldType = fieldMapping.getType();
		String name=fieldMapping.getName();
		Id idAnn = fieldType.getAnnotation(Id.class);
		Version versionAnn = fieldType.getAnnotation(Version.class);
		if (idAnn!=null) {
			mapping.setIdProperty(name);
		}
		if (versionAnn !=null) {
			mapping.setVersionProperty(name);
		}
		super.postProcessProperty(mapping, fieldMapping);
	}

	@Override
	protected void checkAnnotations(Class<?> objectClass, Field field) {
		Annotations.checkForOnlyOneAnnotation(objectClass, field, Id.class, Indexed.class, IndexedInGroup.class,
				IndexedInGroups.class, Version.class);
	}

}
