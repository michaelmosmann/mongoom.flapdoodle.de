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

package de.flapdoodle.mongoom.parser.converter.index;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import de.flapdoodle.mongoom.annotations.index.Indexed;
import de.flapdoodle.mongoom.annotations.index.IndexedInGroup;
import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.exceptions.ObjectMapperException;
import de.flapdoodle.mongoom.mapping.index.EntityIndexDef;
import de.flapdoodle.mongoom.mapping.index.IndexDef;
import de.flapdoodle.mongoom.parser.IMappedProperty;
import de.flapdoodle.mongoom.parser.converter.MappingVisitor;
import de.flapdoodle.mongoom.parser.visitors.IMappingEntityIndexVisitor;
import de.flapdoodle.mongoom.parser.visitors.IMappingIndexVisitor;

public class EntityIndexVisitor implements IMappingEntityIndexVisitor {

	private Map<String, EntityIndexDef> _indexGroups;
	private final MappingVisitor _mappingVisitor;

	private List<Class<?>> _path=Lists.newArrayList();
	private Set<Class<?>> _recursive=Sets.newHashSet();
	
	List<IndexDef> _indexes=Lists.newArrayList();

	public EntityIndexVisitor(MappingVisitor mappingVisitor) {
		_mappingVisitor = mappingVisitor;
	}
	
	public List<IndexDef> getIndex() {
		return Collections.unmodifiableList(_indexes);
	}

	@Override
	public void indexGroups(Map<String, EntityIndexDef> indexGroups) {
		if (_indexGroups != null)
			throw new MappingException(_mappingVisitor._type.getType(), "IndexGroups allready set");
		_indexGroups = indexGroups;

		System.out.println("IndexGroups: " + _indexGroups);
	}

	@Override
	public void property(IMappedProperty property) {
		PropertyIndexVisitor indexVisitor=new PropertyIndexVisitor(this, property);
		indexVisitor.beforeInspection();
		property.inspect(indexVisitor);
		IndexDef indexDef=indexVisitor.afterInspection();
		_indexes.add(indexDef);
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
	
	static class PropertyIndexVisitor implements IMappingIndexVisitor {

		private final EntityIndexVisitor _entityIndexVisitor;
		private final IMappedProperty _property;
		private Indexed _indexed;
		private List<IndexedInGroup> _indexedInGroups;

		private PropertyIndexVisitor(EntityIndexVisitor entityIndexVisitor, IMappedProperty property) {
			_entityIndexVisitor = entityIndexVisitor;
			_property = property;
		}

		private void beforeInspection() {
			_entityIndexVisitor.startProperty(_property);
		}
		
		public IndexDef afterInspection() {
			_entityIndexVisitor.finishProperty(_property);
			return null;
		}

		@Override
		public void indexed(Indexed indexed) {
			_indexed = indexed;
		}

		@Override
		public void indexedInGroups(List<IndexedInGroup> indexedInGroups) {
			_indexedInGroups = indexedInGroups;
		}

		@Override
		public void property(IMappedProperty property) {
			// TODO Auto-generated method stub
			
		}

		public boolean startProperty(IMappedProperty property) {
			return _entityIndexVisitor.startProperty(property);
		}

		public void finishProperty(IMappedProperty property) {
			_entityIndexVisitor.finishProperty(property);
		}

		public boolean isRecursive(IMappedProperty property) {
			return _entityIndexVisitor.isRecursive(property);
		}
		
	}
	
	static class SubPropertyIndexVisitor implements IMappingIndexVisitor{

		private final PropertyIndexVisitor _parent;
		private final IMappedProperty _property;
		
		private Indexed _indexed;
		private List<IndexedInGroup> _indexedInGroups;
		
		private List<SubPropertyIndexVisitor> _properties=Lists.newArrayList();

		public SubPropertyIndexVisitor(PropertyIndexVisitor parent, IMappedProperty property) {
			_parent = parent;
			_property = property;
		}

		@Override
		public void property(IMappedProperty property) {
			if (_parent.startProperty(property)) {
				System.out.println("Property: " + property);
				System.out.println("Property(Class): " + property.getType().getType());
				SubPropertyIndexVisitor indexVisitor = new SubPropertyIndexVisitor(_parent,property);
				_properties.add(indexVisitor);
				property.inspect(indexVisitor);
			}
			if (_parent.isRecursive(property)) {
				if (_indexed!=null) throw new MappingException(property.getType().getType(), "could not index recursive object path ("+_indexed+")");
				if (_indexedInGroups!=null) throw new MappingException(property.getType().getType(), "could not index in groups recursive object path("+_indexedInGroups+")");
			}
			_parent.finishProperty(property);
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