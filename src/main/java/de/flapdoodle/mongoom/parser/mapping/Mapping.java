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

import com.google.common.collect.Maps;

import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.parser.IEntityMapping;
import de.flapdoodle.mongoom.parser.IMappedProperty;
import de.flapdoodle.mongoom.parser.IMappingParserContext;
import de.flapdoodle.mongoom.parser.IMappingParserResult;
import de.flapdoodle.mongoom.parser.IType;
import de.flapdoodle.mongoom.parser.ITypeParser;
import de.flapdoodle.mongoom.parser.ITypeParserFactory;
import de.flapdoodle.mongoom.parser.properties.ClassType;
import de.flapdoodle.mongoom.parser.visitors.IMappingResultVisitor;


public class Mapping implements IMappingParserContext,IMappingParserResult {

	Map<Class<?>,EntityMapping> _entities=Maps.newLinkedHashMap();
	Map<Class<?>,IMappedProperty> _allreadyMapped=Maps.newLinkedHashMap();
	private final ITypeParserFactory _typeParserFactory;
	
	public Mapping(ITypeParserFactory entityParserFactory) {
		_typeParserFactory = entityParserFactory;
	}

	@Override
	public IEntityMapping newEntity(ClassType entityClass) {
		if (_entities.containsKey(entityClass)) throw new MappingException(entityClass.getType(),"allready mapped");
		EntityMapping ret = new EntityMapping(entityClass);
		_entities.put(entityClass.getType(), ret);
		return ret;
	}
	
	@Override
	public IMappedProperty registeredMapping(IType fieldType) {
		return _allreadyMapped.get((Class<?>) fieldType.getType());
	}
	
	@Override
	public void registerMapping(IType fieldType, IMappedProperty mapping) {
		_allreadyMapped.put((Class<?>) fieldType.getType(), mapping);
	}
	
	@Override
	public ITypeParser getParser(IType type) {
		return _typeParserFactory.getParser(type);
	}
	
	@Override
	public String toString() {
		return "Mapping("+_entities.values()+")";
	}

	@Override
	public void inspect(IMappingResultVisitor visitor) {
		for (EntityMapping entity : _entities.values()) {
			visitor.entity(entity);
		}
	}
}
