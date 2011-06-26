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
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;

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
import de.flapdoodle.mongoom.parser.AbstractParser;
import de.flapdoodle.mongoom.parser.FieldType;
import de.flapdoodle.mongoom.parser.IMapping;
import de.flapdoodle.mongoom.parser.IMapProperties;
import de.flapdoodle.mongoom.parser.IType;
import de.flapdoodle.mongoom.parser.ITypeParser;
import de.flapdoodle.mongoom.parser.ITypeParserFactory;
import de.flapdoodle.mongoom.parser.mapping.Mapping;


public class ObjectParserFactory extends AbstractParser implements ITypeParserFactory {

	Set<Class<?>> _illegalTypes=Sets.newHashSet(List.class,Set.class,Map.class);
	
	private final ITypeParserFactory _typeParserFactory;
	
	public ObjectParserFactory(ITypeParserFactory typeParserFactory) {
		_typeParserFactory = typeParserFactory;
	}

	@Override
	public ITypeParser getParser(IType type) {
		Class<?> objectType = type.getType();
		if (_illegalTypes.contains(objectType)) error(type,"Should not be parsed with "+getClass()+", configuration error");
		return new ObjectParser(_typeParserFactory);
	}

	static class ObjectParser extends AbstractObjectParser {

		public ObjectParser(ITypeParserFactory typeParserFactory) {
			super(typeParserFactory);
		}
		
		@Override
		public void parse(IMapping mapping, IMapProperties propertyMapping) {
			parseFields(mapping, propertyMapping);
		}
	}
}
