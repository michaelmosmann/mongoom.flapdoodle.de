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

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.flapdoodle.mongoom.annotations.index.Indexed;
import de.flapdoodle.mongoom.annotations.index.IndexedInGroup;
import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.parser.ClassType;
import de.flapdoodle.mongoom.parser.FieldType;
import de.flapdoodle.mongoom.parser.IEntityMapping;
import de.flapdoodle.mongoom.parser.IMappedProperty;
import de.flapdoodle.mongoom.parser.IMapping;
import de.flapdoodle.mongoom.parser.IType;


public class Mapping implements IMapping {

	Map<Class<?>,EntityMapping> _entities=Maps.newLinkedHashMap();
	Map<Class<?>,IMappedProperty> _allreadyMapped=Maps.newLinkedHashMap();
	
	@Override
	public IEntityMapping newEntity(ClassType entityClass) {
		if (_entities.containsKey(entityClass)) throw new MappingException(entityClass.getType(),"allready mapped");
		EntityMapping ret = new EntityMapping(entityClass);
		_entities.put(entityClass.getType(), ret);
		return ret;
	}
	
	@Override
	public IMappedProperty registeredMapping(FieldType fieldType) {
		return _allreadyMapped.get((Class<?>) fieldType.getType());
	}
	
	@Override
	public void registerMapping(FieldType fieldType, IMappedProperty mapping) {
		_allreadyMapped.put((Class<?>) fieldType.getType(), mapping);
	}
	
	@Override
	public String toString() {
		return "Mapping("+_entities.values()+")";
	}

}
