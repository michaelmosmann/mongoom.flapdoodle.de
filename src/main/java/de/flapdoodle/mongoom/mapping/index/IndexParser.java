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

import sun.awt.datatransfer.DataTransferer.IndexOrderComparator;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.flapdoodle.mongoom.annotations.index.IndexGroup;
import de.flapdoodle.mongoom.annotations.index.IndexGroups;
import de.flapdoodle.mongoom.annotations.index.IndexOption;
import de.flapdoodle.mongoom.annotations.index.Indexed;
import de.flapdoodle.mongoom.annotations.index.IndexedInGroup;
import de.flapdoodle.mongoom.annotations.index.IndexedInGroups;
import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.mapping.converter.annotations.IAnnotated;


public class IndexParser {

	public static <T> void processFieldIndexes(IndexContext<T> indexContext) {
		List<IndexDef> subIndexes = indexContext.getFieldConverter().getIndexes();
		if (subIndexes != null) {
			for (IndexDef def : subIndexes) {
				if (def.group() != null) {
					EntityIndexDef gdef = indexContext.getIndexGroup(def.group());
					if (gdef == null)
						throw new MappingException(indexContext.getEntityClass(), "Field " + indexContext.getField() + ", IndexGroup " + def.group()
								+ " not defined (in FieldType)");
					FieldIndex field = def.fields().get(0);
					gdef.addField(new FieldIndex(indexContext.getFieldName() + "." + field.name(), field.direction(), field.priority()));
				} else {
					List<FieldIndex> indexFields = Lists.newArrayList();
					for (FieldIndex fi : def.fields()) {
						indexFields.add(new FieldIndex(indexContext.getFieldName() + "." + fi.name(), fi.direction(), fi.priority()));
					}
					indexContext.addIndexDefinitions(new IndexDef(def.name(), indexFields, def.unique(), def.dropDups()));
				}
			}
		}
	}

	public static <T> void extractIndex(IndexContext<T> indexContext) {
		IAnnotated field = indexContext.getField();
		Class<T> entityClass = indexContext.getEntityClass();
		
		Indexed indexedAnn=null;
		IndexedInGroup[] idxInGAnnList = null;
		OneOrOther<Indexed, IndexedInGroup[]> indexOrIndexGroups = getIndexDef(entityClass, field);
		if (indexOrIndexGroups.getOne()!=null) indexedAnn=indexOrIndexGroups.getOne();
		else idxInGAnnList=indexOrIndexGroups.getOther();
	
		if (indexedAnn != null) {
			List<IndexDef> subIndexes = indexContext.getFieldConverter().getIndexes();
			if ((subIndexes != null) && (!subIndexes.isEmpty()))
				throw new MappingException(entityClass, "Field " + field + " is indexed, but type has index too");
	
			IndexOption options = indexedAnn.options();
			String name = indexedAnn.name();
			if (name.length() == 0)
				name = null;
			indexContext.addIndexDefinitions(new IndexDef(name, Lists.newArrayList(new FieldIndex(indexContext.getFieldName(), indexedAnn.direction(), 0)),
					options.unique(), options.dropDups()));
		} else {
			if (idxInGAnnList != null) {
				for (IndexedInGroup iig : idxInGAnnList) {
					String groupName = iig.group();
					if (indexContext.isEntity()) {
						EntityIndexDef def = indexContext.getIndexGroup(groupName);
						if (def == null)
							throw new MappingException(entityClass, "Field " + field + ", IndexGroup " + groupName + " not defined");
						def.addField(new FieldIndex(indexContext.getFieldName(), iig.direction(), iig.priority()));
					} else {
						indexContext.addIndexDefinitions(new IndexDef(groupName, new FieldIndex(indexContext.getFieldName(), iig.direction(), iig.priority())));
					}
				}
			} else {
				processFieldIndexes(indexContext);
			}
		}
	}
	
	public static OneOrOther<Indexed, IndexedInGroup[]> getIndexDef(Class<?> entityClass, IAnnotated field) {
		Indexed indexedAnn = field.getAnnotation(Indexed.class);
		IndexedInGroup indexedInGroupAnn = field.getAnnotation(IndexedInGroup.class);
		IndexedInGroups indexedInGroupsAnn = field.getAnnotation(IndexedInGroups.class);
	
		if ((indexedAnn != null) && (indexedInGroupAnn != null))
			throw new MappingException(entityClass, "Field " + field + " is indexed and indexedInGroup");
		if ((indexedAnn != null) && (indexedInGroupsAnn != null))
			throw new MappingException(entityClass, "Field " + field + " is indexed and indexedInGroups");
		if ((indexedInGroupAnn != null) && (indexedInGroupsAnn != null))
			throw new MappingException(entityClass, "Field " + field
					+ " is indexedInGroup and indexedInGroups (use indexedInGroups only)");
		if ((indexedInGroupsAnn != null) && (indexedInGroupsAnn.value().length == 0))
			throw new MappingException(entityClass, "Field " + field
					+ " is indexedInGroups but in which one? (Annotation value is empty)");
	
		IndexedInGroup[] idxInGAnnList = null;
		if (indexedInGroupAnn != null)
			idxInGAnnList = new IndexedInGroup[] {indexedInGroupAnn};
		if (indexedInGroupsAnn != null)
			idxInGAnnList = indexedInGroupsAnn.value();

		return new OneOrOther<Indexed, IndexedInGroup[]>(indexedAnn, idxInGAnnList);
	}

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
			map.put(ig.group(), new EntityIndexDef(name, options.unique(), options.dropDups()));
		}
		return map;
	}

}
