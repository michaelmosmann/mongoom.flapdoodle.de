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

package de.flapdoodle.mongoom.parser.converter;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import de.flapdoodle.mongoom.mapping.Const;
import de.flapdoodle.mongoom.mapping.index.IndexDef;
import de.flapdoodle.mongoom.parser.IMappedProperty;
import de.flapdoodle.mongoom.parser.converter.index.EntityIndexVisitor;
import de.flapdoodle.mongoom.parser.converter.index.SimpleEntityIndexVisitor;
import de.flapdoodle.mongoom.parser.mapping.EntityMapping;
import de.flapdoodle.mongoom.parser.naming.IEntityNamingFactory;
import de.flapdoodle.mongoom.parser.naming.IPropertyNamingFactory;
import de.flapdoodle.mongoom.parser.properties.ClassType;
import de.flapdoodle.mongoom.parser.visitors.IMappingResultVisitor;

public class MappingVisitor implements IMappingResultVisitor {

	public ClassType _type;

	private final IEntityNamingFactory _entityNaming;
	private final IPropertyNamingFactory _propertyNaming;

	private final Map<String, List<IndexDef>> _indexMap = Maps.newHashMap();

	public MappingVisitor(IEntityNamingFactory entityNaming, IPropertyNamingFactory propertyNaming) {
		_entityNaming = entityNaming;
		_propertyNaming = propertyNaming;
	}

	@Override
	public void entity(EntityMapping entity) {
		System.out.println("Entity: " + entity);
		System.out.println("EntityName: " + _entityNaming.getEntityName(entity.getType()));

		_type = entity.getType();

		SimpleEntityIndexVisitor indexVisitor = new SimpleEntityIndexVisitor(this);
		entity.inspect(indexVisitor);
		_indexMap.put(_entityNaming.getEntityName(entity.getType()), indexVisitor.getIndex());
	}
	
	public Map<String, List<IndexDef>> getIndexes() {
		return Collections.unmodifiableMap(_indexMap);
	}

	public String getPropertyName(List<IMappedProperty> properties) {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (IMappedProperty property : properties) {
			if (first)
				first = false;
			else
				sb.append(Const.FIELDNAME_SEP);
			String name = _propertyNaming.getPropertyName(property.getType());
			sb.append(name);
		}
		return sb.toString();
	}
}
