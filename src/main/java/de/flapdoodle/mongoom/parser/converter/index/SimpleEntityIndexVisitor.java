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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import de.flapdoodle.mongoom.annotations.index.Indexed;
import de.flapdoodle.mongoom.annotations.index.IndexedInGroup;
import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.exceptions.NotImplementedException;
import de.flapdoodle.mongoom.exceptions.ObjectMapperException;
import de.flapdoodle.mongoom.mapping.index.EntityIndexDef;
import de.flapdoodle.mongoom.mapping.index.FieldIndex;
import de.flapdoodle.mongoom.mapping.index.IndexDef;
import de.flapdoodle.mongoom.parser.IMappedProperty;
import de.flapdoodle.mongoom.parser.converter.MappingVisitor;
import de.flapdoodle.mongoom.parser.visitors.IMappingEntityIndexVisitor;
import de.flapdoodle.mongoom.parser.visitors.IMappingIndexVisitor;

public class SimpleEntityIndexVisitor implements IMappingEntityIndexVisitor {

	private final MappingVisitor _mappingVisitor;
	private Map<String, EntityIndexDef> _indexGroups;

	private List<Class<?>> _path = Lists.newArrayList();
	private Set<Class<?>> _recursive = Sets.newHashSet();
	private List<IndexDef> _indexDefs = Lists.newArrayList();

	public SimpleEntityIndexVisitor(MappingVisitor mappingVisitor) {
		_mappingVisitor = mappingVisitor;

	}

	public List<IndexDef> getIndex() {
		List<IndexDef> ret = Lists.newArrayList();
		ret.addAll(_indexDefs);
		ret.addAll(_indexGroups.values());
		return ret;
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
		PropertyIndexVisitor indexVisitor = new PropertyIndexVisitor(null,property);
		property.inspect(indexVisitor);
		_indexDefs.addAll(indexVisitor.getIndex());
	}

	protected boolean startProperty(IMappedProperty property) {
		Class<?> type = property.getType().getType();
		int idx = _path.indexOf(type);
		boolean ret = true;
		if (idx != -1) {
			ret = false;
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
			throw new ObjectMapperException("Type does not match: " + type + "!=" + _path.get(lastIndex));
		}
	}

	protected boolean isRecursive(IMappedProperty property) {
		Class<?> type = property.getType().getType();
		return _recursive.contains(type);
	}
	
	protected String getPropertyName(List<IMappedProperty> properties) {
		return _mappingVisitor.getPropertyName(properties);
	}

	class PropertyIndexVisitor implements IMappingIndexVisitor {

		private final PropertyIndexVisitor _parent;
		
		private Indexed _indexed;
		private List<IndexedInGroup> _indexedInGroups;
		private final IMappedProperty _property;
		
		List<PropertyIndexVisitor> _childs=Lists.newArrayList();

		public PropertyIndexVisitor(PropertyIndexVisitor parent, IMappedProperty property) {
			_parent = parent;
			_property = property;
		}

		public List<IndexDef> getIndex() {
			ArrayList<IndexDef> ret = Lists.newArrayList();
			if (_indexed!=null) {
				List<FieldIndex> fields=Lists.newArrayList(new FieldIndex(getPropertyName(getPath()), _indexed.direction(), 0));
				ret.add(new IndexDef(_indexed.name(), fields, _indexed.options().unique(), _indexed.options().dropDups(), _indexed.options().sparse()));
			}
			if (_indexedInGroups!=null) {
				for (IndexedInGroup indexedInGroup : _indexedInGroups) {
					EntityIndexDef entityIndexDef = _indexGroups.get(indexedInGroup.group());
					if (entityIndexDef==null) throw new MappingException(_property.getType().getType(),"Could not find IndexGroup "+indexedInGroup.group());
					entityIndexDef.addField(new FieldIndex(getPropertyName(getPath()), indexedInGroup.direction(), indexedInGroup.priority()));
				}
			}
			return ret;
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
			if (startProperty(property)) {
				System.out.println("Property: " + property);
				System.out.println("Property(Class): " + property.getType().getType());
				PropertyIndexVisitor indexVisitor = new PropertyIndexVisitor(this,property);
				_childs.add(indexVisitor);
				property.inspect(indexVisitor);
			}
			if (isRecursive(property)) {
				if (_indexed != null)
					throw new MappingException(property.getType().getType(), "could not index recursive object path (" + _indexed
							+ ")");
				if (_indexedInGroups != null)
					throw new MappingException(property.getType().getType(), "could not index in groups recursive object path("
							+ _indexedInGroups + ")");
			}
			finishProperty(property);
		}

		protected List<IMappedProperty> getPath() {
			List<IMappedProperty> ret=Lists.newArrayList();
			if (_parent!=null) {
				ret.addAll(_parent.getPath());
			}
			ret.add(_property);
			return ret;
		}

	}
	
}
