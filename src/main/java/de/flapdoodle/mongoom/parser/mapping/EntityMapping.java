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
import de.flapdoodle.mongoom.mapping.index.EntityIndexDef;
import de.flapdoodle.mongoom.parser.ClassType;
import de.flapdoodle.mongoom.parser.IEntityMapping;
import de.flapdoodle.mongoom.parser.IMapProperties;
import de.flapdoodle.mongoom.parser.IMappedProperty;
import de.flapdoodle.mongoom.parser.IType;


public class EntityMapping extends AbstractPropertyMapping implements IEntityMapping{

	private String _versionProperty;
	private String _idProperty;
	private Map<String, EntityIndexDef> _indexGroupMap;

	public EntityMapping(ClassType entityClass) {
		super(entityClass);
	}
	
	public void setVersionProperty(String versionProperty) {
		if (_versionProperty!=null) error("versioned property allready set: "+_versionProperty+"->"+versionProperty);
		_versionProperty = versionProperty;
		
	}
	
	@Override
	public void setIdProperty(String idProperty) {
		if (_idProperty!=null) error("id property allready set: "+_idProperty+"->"+idProperty);
		_idProperty = idProperty;
	}

	@Override
	public void setIndexGroups(Map<String, EntityIndexDef> indexGroupMap) {
		_indexGroupMap = indexGroupMap;
		
	}

	@Override
	public String toString() {
		return "Entity("+getType()+",id="+_idProperty+",version="+_versionProperty+",indexes="+_indexGroupMap+",properties="+_properties+")";
	}
}
