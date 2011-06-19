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

package de.flapdoodle.mongoom.mapping.converter.factories;

import java.lang.reflect.Type;
import java.util.List;
import java.util.logging.Logger;

import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.logging.LogConfig;
import de.flapdoodle.mongoom.mapping.ITypeConverter;
import de.flapdoodle.mongoom.mapping.Mapper;
import de.flapdoodle.mongoom.mapping.MappingContext;
import de.flapdoodle.mongoom.mapping.converter.ITypeConverterFactory;
import de.flapdoodle.mongoom.mapping.converter.annotations.IAnnotated;
import de.flapdoodle.mongoom.mapping.converter.generics.TypeExtractor;
import de.flapdoodle.mongoom.types.Reference;

public class ReferenceConverterFactory<T extends List> implements ITypeConverterFactory<T> {

	private static final Logger _logger = LogConfig.getLogger(ReferenceConverterFactory.class);

	@Override
	public ITypeConverter<T> converter(Mapper mapper, MappingContext context, Class<T> type, Type genericType, IAnnotated annotations) {
		if (Reference.class.isAssignableFrom(type)) {
			Type parameterizedClass = TypeExtractor.getParameterizedClass(context.getEntityClass(), genericType, 0);
			_logger.severe("ParamType: " + parameterizedClass + " for " + type);
			if (parameterizedClass != null) {
				if (parameterizedClass instanceof Class) {
					//				ITypeConverter<Object> converter = mapper.map(ObjectId.class, ObjectId.class.getGenericSuperclass(), null);
					return new ReferenceConverter((Class) parameterizedClass);
				} else {
					throw new MappingException(context.getEntityClass(), "Type is not a Class: " + parameterizedClass);
				}
			}
		}
		return null;
	}
}
