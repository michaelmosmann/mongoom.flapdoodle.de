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
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import de.flapdoodle.mongoom.annotations.index.Indexed;
import de.flapdoodle.mongoom.annotations.index.IndexedInGroup;
import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.exceptions.ObjectMapperException;
import de.flapdoodle.mongoom.mapping.IMappingConfig;
import de.flapdoodle.mongoom.mapping.index.EntityIndexDef;
import de.flapdoodle.mongoom.parser.IMappedProperty;
import de.flapdoodle.mongoom.parser.IMappingParserResult;
import de.flapdoodle.mongoom.parser.mapping.EntityMapping;
import de.flapdoodle.mongoom.parser.naming.IEntityNamingFactory;
import de.flapdoodle.mongoom.parser.naming.IPropertyNamingFactory;
import de.flapdoodle.mongoom.parser.properties.ClassType;
import de.flapdoodle.mongoom.parser.visitors.IMappingEntityIndexVisitor;
import de.flapdoodle.mongoom.parser.visitors.IMappingIndexVisitor;
import de.flapdoodle.mongoom.parser.visitors.IMappingResultVisitor;

public class ObjectMapper {

	public ObjectMapper(IEntityNamingFactory entityNaming, IPropertyNamingFactory propertyNaming,
			IMappingParserResult mappingResult) {
		mappingResult.inspect(new MappingVisitor(entityNaming, propertyNaming));
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
			System.out.println("Entity: " + entity);
			System.out.println("EntityName: " + _entityNaming.getEntityName(entity.getType()));

			_type = entity.getType();

			entity.inspect(new EntityIndexVisitor(this));

		}
	}

	static class EntityIndexVisitor implements IMappingEntityIndexVisitor {

		private Map<String, EntityIndexDef> _indexGroups;
		private final MappingVisitor _mappingVisitor;

		private List<Class<?>> _path=Lists.newArrayList();
		private Set<Class<?>> _recursive=Sets.newHashSet();

		public EntityIndexVisitor(MappingVisitor mappingVisitor) {
			_mappingVisitor = mappingVisitor;
		}

		@Override
		public void indexGroups(Map<String, EntityIndexDef> indexGroups) {
			if (_indexGroups != null)
				throw new MappingException(_mappingVisitor._type.getType(), "IndexGroups allready set");
			_indexGroups = indexGroups;

			System.out.println("IndexGroups: " + _indexGroups);
		}

		@Override
		public IMappingIndexVisitor indexVisitor() {
			return new IndexVisitor(this);
		}

		protected boolean startProperty(IMappedProperty property) {
			Class<?> type = property.getType().getType();
			int idx=_path.indexOf(type);
			boolean ret = true;
			if (idx!=-1) {
				ret=false;
				for (Class<?> pathType : _path) {
					_recursive.add(pathType);
				}
			}
			_path.add(type);
			return ret;
		}

		protected void finishProperty(IMappedProperty property) {
			Class<?> type = property.getType().getType();
			int lastIndex = _path.size() - 1;
			if (_path.get(lastIndex).equals(type)) {
				_path.remove(lastIndex);
			} else {
				throw new ObjectMapperException("Type does not match: "+type+"!="+_path.get(lastIndex));
			}
		}
		
		protected boolean isRecursive(IMappedProperty property) {
			Class<?> type = property.getType().getType();
			return _recursive.contains(type);
		}
	}

	static class IndexVisitor implements IMappingIndexVisitor {

		EntityIndexVisitor _entityVisitor;
		private Indexed _indexed;
		private List<IndexedInGroup> _indexedInGroups;

		public IndexVisitor(EntityIndexVisitor entityVisitor) {
			_entityVisitor = entityVisitor;
		}

		@Override
		public void property(IMappedProperty property) {
			if (_entityVisitor.startProperty(property)) {
				System.out.println("Property: " + property);
				System.out.println("Property(Class): " + property.getType().getType());
				property.inspect(new IndexVisitor(_entityVisitor));
			}
			if (_entityVisitor.isRecursive(property)) {
				if (_indexed!=null) throw new MappingException(property.getType().getType(), "could not index recursive object path ("+_indexed+")");
				if (_indexedInGroups!=null) throw new MappingException(property.getType().getType(), "could not index in groups recursive object path("+_indexedInGroups+")");
			}
			_entityVisitor.finishProperty(property);
		}

		@Override
		public void indexed(Indexed indexed) {
			System.out.println("Indexed: " + indexed);
			_indexed = indexed;
		}

		@Override
		public void indexedInGroups(List<IndexedInGroup> indexedInGroups) {
			System.out.println("IndexedInGroup: " + indexedInGroups);
			_indexedInGroups = indexedInGroups;
		}

	}

}
