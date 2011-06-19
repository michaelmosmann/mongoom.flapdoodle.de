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

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.flapdoodle.mongoom.mapping.converter.ITypeConverterFactory;
import de.flapdoodle.mongoom.mapping.converter.factories.CollectionConverterFactory;
import de.flapdoodle.mongoom.mapping.converter.factories.EnumConverterFactory;
import de.flapdoodle.mongoom.mapping.converter.factories.RawConverterFactory;
import de.flapdoodle.mongoom.mapping.converter.factories.ReferenceConverterFactory;
import de.flapdoodle.mongoom.mapping.naming.EntityAnnotationNamingFactory;
import de.flapdoodle.mongoom.mapping.naming.FieldAnnotationNamingFactory;
import de.flapdoodle.mongoom.mapping.naming.PrefixFieldNamingFactory;
import de.flapdoodle.mongoom.mapping.versions.StringVersionFactory;

public class MappingConfig implements IMappingConfig {

	List<ITypeConverterFactory<?>> _converterFactories = Lists.newArrayList();

	List<IEntityNamingFactory> _entityNamingFactories = Lists.newArrayList();

	List<IFieldNamingFactory> _fieldNamingFactories = Lists.newArrayList();

	Map<Class<?>, IVersionFactory<?>> _versionFactories = Maps.newHashMap();

	private MappingConfig() {

	}

	public static IMappingConfig getDefaults() {
		MappingConfig ret = new MappingConfig();

		ret._converterFactories.add(new RawConverterFactory());
		ret._converterFactories.add(new EnumConverterFactory());
		ret._converterFactories.add(new CollectionConverterFactory());
		ret._converterFactories.add(new ReferenceConverterFactory());
		
		ret._entityNamingFactories.add(new EntityAnnotationNamingFactory());
		
		ret._fieldNamingFactories.add(new FieldAnnotationNamingFactory());
		ret._fieldNamingFactories.add(new PrefixFieldNamingFactory());
		
		ret._versionFactories.put(String.class, new StringVersionFactory());
		return ret;
	}

	@Override
	public List<ITypeConverterFactory<?>> getConverterFactories() {
		return _converterFactories;
	}

	@Override
	public List<IFieldNamingFactory> getFieldNamingFactories() {
		return _fieldNamingFactories;
	}

	@Override
	public List<IEntityNamingFactory> getEntityNamingFactories() {
		return _entityNamingFactories;
	}

	@Override
	public Map<Class<?>, IVersionFactory<?>> getVersionFactories() {
		return _versionFactories;
	}
}
