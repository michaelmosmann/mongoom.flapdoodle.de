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

package de.flapdoodle.mongoom.parser;

import java.util.Set;

import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.parser.mapping.Mapping;
import de.flapdoodle.mongoom.parser.properties.ClassType;


public class MappingParser {

	public static IMappingParserResult map(Set<Class<?>> entityClasses,IEntityParserFactory entityParserFactory) {
		Mapping ret=new Mapping(entityParserFactory.getTypeParserFactory());
		for (Class<?> entityClass : entityClasses) {
			IEntityParser typeParser=entityParserFactory.getParser(ClassType.of(entityClass));
			if (typeParser==null) error(entityClass,"No TypeParser found");
			ClassType classType = ClassType.of(entityClass);
			typeParser.parse(ret,classType);
		}
		return ret;
	}
	
	static void error(Class<?> entityClass, String message) {
		throw new MappingException(entityClass, message);
	}
}
