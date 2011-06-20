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

package de.flapdoodle.mongoom.mapping.index;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import de.flapdoodle.mongoom.mapping.ITypeConverter;

public class IndexContext<T> {

	private Class<T> _entityClass;
	private boolean _isEntity;
	private Field _f;
	private String _fieldName;
	private ITypeConverter _fieldConverter;
	private Map<String, EntityIndexDef> _indexGroupMap;
	private List<IndexDef> _indexDefinitions;

	public IndexContext(Class<T> entityClass, boolean isEntity, Field f, String fieldName, ITypeConverter fieldConverter,
			Map<String, EntityIndexDef> indexGroupMap, List<IndexDef> indexDefinitions) {
		_entityClass = entityClass;
		_isEntity = isEntity;
		_f = f;
		_fieldName = fieldName;
		_fieldConverter = fieldConverter;
		_indexGroupMap = indexGroupMap;
		_indexDefinitions = indexDefinitions;
	}

	public Class<T> getEntityClass() {
		return _entityClass;
	}

	public boolean isEntity() {
		return _isEntity;
	}

	public Field getF() {
		return _f;
	}

	public String getFieldName() {
		return _fieldName;
	}

	public ITypeConverter getFieldConverter() {
		return _fieldConverter;
	}

	public Map<String, EntityIndexDef> getIndexGroupMap() {
		return _indexGroupMap;
	}

	public List<IndexDef> getIndexDefinitions() {
		return _indexDefinitions;
	}

}
