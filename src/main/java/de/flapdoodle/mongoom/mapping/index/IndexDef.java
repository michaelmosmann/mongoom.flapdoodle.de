/**
 * Copyright (C) 2010 Michael Mosmann <michael@mosmann.de>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.flapdoodle.mongoom.mapping.index;

import java.util.List;

import com.google.common.collect.Lists;

public class IndexDef {

	private final String _name;
	private final String _group;
	protected final List<FieldIndex> _fields;
	private final boolean _unique;
	private final boolean _dropDups;

	public IndexDef(String name, List<FieldIndex> fields, boolean unique, boolean dropDups) {
		_group = null;
		_name = name;
		_fields = fields;
		_unique = unique;
		_dropDups = dropDups;
	}

	public IndexDef(String group, FieldIndex field) {
		_group = group;
		_name = null;
		_fields = Lists.newArrayList(field);
		_unique = false;
		_dropDups = false;
	}

	public String group() {
		return _group;
	}

	public String name() {
		return _name;
	}

	public List<FieldIndex> fields() {
		return _fields;
	}

	public boolean unique() {
		return _unique;
	}

	public boolean dropDups() {
		return _dropDups;
	}

	@Override
	public String toString() {
		StringBuilder sfields = new StringBuilder();
		sfields.append("{");
		for (FieldIndex f : _fields) {
			sfields.append(f);
			sfields.append(",");
		}
		sfields.append("}");
		return "Index{" + (_group != null
				? "group:" + _group + ", "
				: "") + (_name != null
				? "name:" + _name + ", "
				: "") + "key: " + sfields + " ,unique:" + _unique + " ,dropDups:" + _dropDups + "}";
	}
}
