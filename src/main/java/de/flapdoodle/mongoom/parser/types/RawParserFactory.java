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

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.bson.types.ObjectId;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.flapdoodle.mongoom.logging.LogConfig;
import de.flapdoodle.mongoom.mapping.ITypeConverter;
import de.flapdoodle.mongoom.mapping.Mapper;
import de.flapdoodle.mongoom.mapping.MappingContext;
import de.flapdoodle.mongoom.mapping.converter.annotations.IAnnotated;
import de.flapdoodle.mongoom.mapping.converter.factories.RawConverter;
import de.flapdoodle.mongoom.mapping.converter.factories.RawConverterFactory;
import de.flapdoodle.mongoom.parser.IMapping;
import de.flapdoodle.mongoom.parser.IMapProperties;
import de.flapdoodle.mongoom.parser.IType;
import de.flapdoodle.mongoom.parser.ITypeParser;
import de.flapdoodle.mongoom.parser.ITypeParserFactory;
import de.flapdoodle.mongoom.parser.mapping.Mapping;

public class RawParserFactory implements ITypeParserFactory {

	private static final Logger _logger = LogConfig.getLogger(RawParserFactory.class);

	static Set<Class<?>> _types = Sets.newHashSet();
	static {
		_types.add(String.class);
		_types.add(Integer.class);
		_types.add(Long.class);
		_types.add(int.class);
		_types.add(long.class);
		_types.add(Date.class);
		_types.add(ObjectId.class);
		_types.add(Double.class);
		_types.add(double.class);
		_types.add(Boolean.class);
		_types.add(boolean.class);
	}

	static Map<Class<?>, Class<?>> _objectType = Maps.newHashMap();
	static {
		_objectType.put(int.class, Integer.class);
		_objectType.put(long.class, Long.class);
		_objectType.put(boolean.class, Boolean.class);
		_objectType.put(double.class, Double.class);
		_objectType.put(float.class, Float.class);
	}

	@Override
	public ITypeParser getParser(IType type) {
		if (_types.contains(type.getType())) {
			_logger.severe("Using RawParser for " + type);
			Class<?> objectClass = _objectType.get(type.getType());
			if (objectClass == null)
				objectClass = type.getType();
			return new RawParser(objectClass);
		}
		return null;
	}

	static class RawParser implements ITypeParser {

		private final Class<?> _objectClass;

		public RawParser(Class<?> objectClass) {
			_objectClass = objectClass;
		}

		@Override
		public void parse(IMapProperties mapping, IType clazz) {
			// TODO Auto-generated method stub

		}

	}
}
