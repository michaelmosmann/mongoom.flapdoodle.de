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

package de.flapdoodle.mongoom.parser.types.collections;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import de.flapdoodle.mongoom.logging.LogConfig;
import de.flapdoodle.mongoom.mapping.converter.generics.TypeExtractor;
import de.flapdoodle.mongoom.parser.IMapProperties;
import de.flapdoodle.mongoom.parser.IMappingParserContext;
import de.flapdoodle.mongoom.parser.IType;
import de.flapdoodle.mongoom.parser.ITypeParser;
import de.flapdoodle.mongoom.parser.properties.EmbeddedType;


public class ListParser extends AbstractCollectionParser {

	
	private static final Logger _logger = LogConfig.getLogger(ListParser.class);

	@Override
	public void parse(IMappingParserContext mappingParserContext, IMapProperties mapProperties) {
		IType propertyType = mapProperties.getType();
		
		Type parameterizedClass = TypeExtractor.getParameterizedClass(propertyType.getType(), propertyType.getGenericType(), 0);
		_logger.severe("ParamType: " + parameterizedClass + " for " + propertyType);

		ITypeParser typeParser=mappingParserContext.getParser(new EmbeddedType((Class<?>) parameterizedClass));
		
//		ContainerType.of(propertyType, parameterizedClass);

//		typeParser.parse(mappingParserContext, mapProperties.);
//		ITypeParser parser = _typeParserFactory.getParser(new EmbeddedType((Class<?>) parameterizedClass));
//		parser.parse(mappingParserContext, mapProperties);
//		typeParser.parse(mappingParserContext, mapProperties);
	}

}
