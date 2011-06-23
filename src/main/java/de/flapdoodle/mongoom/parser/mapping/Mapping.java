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

package de.flapdoodle.mongoom.parser.mapping;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;

import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.parser.IEntityMapping;


public class Mapping implements IEntityMapping {

	Map<Class<?>,EntityMapping> _entities=Maps.newHashMap();
	
	@Override
	public EntityMapping newEntity(Class<?> entityClass) {
		if (_entities.containsKey(entityClass)) throw new MappingException(entityClass,"allready mapped");
		EntityMapping ret = new EntityMapping(entityClass);
		_entities.put(entityClass, ret);
		return ret;
	}

}
