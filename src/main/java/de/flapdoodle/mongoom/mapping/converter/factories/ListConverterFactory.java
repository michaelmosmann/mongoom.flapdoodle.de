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

import de.flapdoodle.logging.LogConfig;
import de.flapdoodle.mongoom.mapping.ITypeConverter;
import de.flapdoodle.mongoom.mapping.Mapper;
import de.flapdoodle.mongoom.mapping.converter.ITypeConverterFactory;
import de.flapdoodle.mongoom.mapping.converter.generics.GenericTypeExtractor;
import de.flapdoodle.mongoom.mapping.converter.generics.TypeExtractor;

public class ListConverterFactory<T extends List> implements ITypeConverterFactory<T>
{
	private static final Logger _logger = LogConfig.getLogger(ListConverterFactory.class);
	
	@Override
	public ITypeConverter<T> converter(Mapper mapper, Class<?> entityClass, Class<T> type, Type genericType)
	{
		if (List.class.isAssignableFrom(type))
		{
			Class parameterizedClass = TypeExtractor.getParameterizedClass(entityClass, genericType,0);
			_logger.severe("ParamType: "+parameterizedClass+" for "+type);
			if (parameterizedClass!=null)
			{
				ITypeConverter<Object> converter = mapper.map(entityClass, parameterizedClass, parameterizedClass.getGenericSuperclass(), null);
				return new ListConverter<T>(converter);
			}
		}
		return null;
	}
}
