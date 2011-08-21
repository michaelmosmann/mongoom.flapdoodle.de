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

import java.util.List;

import com.google.common.collect.Lists;

import de.flapdoodle.mongoom.annotations.index.Indexed;
import de.flapdoodle.mongoom.annotations.index.IndexedInGroup;
import de.flapdoodle.mongoom.parser.IContainerType;
import de.flapdoodle.mongoom.parser.IMappedProperty;
import de.flapdoodle.mongoom.parser.visitors.IMappingIndexVisitor;


public class ContainerMapping  extends AbstractPropertyMapping<IContainerType> implements IMappedProperty {

	Indexed _indexed;
	List<IndexedInGroup> _indexedInGroups;

	public ContainerMapping(IContainerType type) {
		super(type);
	}

	@Override
	public void setIndex(Indexed indexed) {
		_indexed = indexed;
		if (_indexedInGroups!=null) error("allready indexedInGroup");
	}

	@Override
	public void setIndexedInGroup(IndexedInGroup[] indexedInGroups) {
		_indexedInGroups = Lists.newArrayList(indexedInGroups);
		if (_indexed!=null) error("allready indexed");
	}

	@Override
	public void inspect(IMappingIndexVisitor indexVisitor) {
		if (_indexed!=null) indexVisitor.indexed(_indexed);
		else if (_indexedInGroups!=null) indexVisitor.indexedInGroups(_indexedInGroups);
		for (IMappedProperty property : getProperties()) {
			indexVisitor.property(property);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append("Field(type=").append(getType()).append(",");
		if (_indexed!=null) sb.append(",indexed=").append(_indexed);
		if (_indexedInGroups!=null) sb.append(",indexedinGroup=").append(_indexedInGroups);
		sb.append(super.toString()).append(")");
		return sb.toString();
	}


}
