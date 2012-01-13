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

import java.util.Map;

import com.google.common.collect.Maps;

import de.flapdoodle.mongoom.annotations.index.IndexGroup;
import de.flapdoodle.mongoom.annotations.index.IndexGroups;
import de.flapdoodle.mongoom.annotations.index.IndexOption;


public class IndexParser {


	private static IndexGroup[] getIndexGroups(Class<?> entityClass) {
		IndexGroup[] list = {};
		IndexGroups indexGroups = entityClass.getAnnotation(IndexGroups.class);
		if (indexGroups != null) {
			list = indexGroups.value();
		} else {
			IndexGroup indexGroup = entityClass.getAnnotation(IndexGroup.class);
			if (indexGroup != null) {
				list = new IndexGroup[] {indexGroup};
			}
		}
		return list;
	}

	public static Map<String, EntityIndexDef> getIndexGroupMap(Class<?> entityClass) {
		Map<String, EntityIndexDef> map = Maps.newHashMap();
		IndexGroup[] indexGroups = getIndexGroups(entityClass);
		for (IndexGroup ig : indexGroups) {
			IndexOption options = ig.options();
			String name = ig.name();
			if (name.length() == 0)
				name = null;
			map.put(ig.group(), new EntityIndexDef(name, options.unique(), options.dropDups(), options.sparse()));
		}
		return map;
	}

}
