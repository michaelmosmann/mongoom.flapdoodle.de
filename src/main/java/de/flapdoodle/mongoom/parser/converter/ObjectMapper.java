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

import java.util.List;
import java.util.Map;

import de.flapdoodle.mongoom.annotations.index.Indexed;
import de.flapdoodle.mongoom.annotations.index.IndexedInGroup;
import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.mapping.IMappingConfig;
import de.flapdoodle.mongoom.mapping.index.EntityIndexDef;
import de.flapdoodle.mongoom.parser.IMappedProperty;
import de.flapdoodle.mongoom.parser.IMappingParserResult;
import de.flapdoodle.mongoom.parser.mapping.EntityMapping;
import de.flapdoodle.mongoom.parser.naming.IEntityNamingFactory;
import de.flapdoodle.mongoom.parser.naming.IPropertyNamingFactory;
import de.flapdoodle.mongoom.parser.properties.ClassType;
import de.flapdoodle.mongoom.parser.visitors.IMappingIndexVisitor;
import de.flapdoodle.mongoom.parser.visitors.IMappingResultVisitor;


public class ObjectMapper {


	public ObjectMapper(IEntityNamingFactory entityNaming, IPropertyNamingFactory propertyNaming, IMappingParserResult mappingResult) {
		mappingResult.inspect(new MappingVisitor(entityNaming,propertyNaming));
	}
	
	static class MappingVisitor implements IMappingResultVisitor {
		
		public ClassType _type;
		
		private final IEntityNamingFactory _entityNaming;
		private final IPropertyNamingFactory _propertyNaming;

		public MappingVisitor(IEntityNamingFactory entityNaming, IPropertyNamingFactory propertyNaming) {
			_entityNaming = entityNaming;
			_propertyNaming = propertyNaming;
		}

		@Override
		public void entity(EntityMapping entity) {
			System.out.println("Entity: "+entity);
			System.out.println("EntityName: "+_entityNaming.getEntityName(entity.getType()));
			
			_type=entity.getType();
			
			entity.inspect(new IndexVisitor(this));
			
		}
	}
	
	static class IndexVisitor implements IMappingIndexVisitor {

		private Map<String, EntityIndexDef> _indexGroups;
		private final MappingVisitor _mappingVisitor;

		public IndexVisitor(MappingVisitor mappingVisitor) {
			_mappingVisitor = mappingVisitor;
		}

		@Override
		public void indexGroups(Map<String, EntityIndexDef> indexGroups) {
			if (_indexGroups!=null) throw new MappingException(_mappingVisitor._type.getType(),"IndexGroups allready set"); 
			_indexGroups = indexGroups;
			
			System.out.println("IndexGroups: "+_indexGroups);
		}
		
		@Override
		public void property(IMappedProperty property) {
			
		}
		
		@Override
		public void indexed(Indexed indexed) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void indexedInGroup(List<IndexedInGroup> indexedInGroups) {
			// TODO Auto-generated method stub
			
		}
		
		
	}
}
