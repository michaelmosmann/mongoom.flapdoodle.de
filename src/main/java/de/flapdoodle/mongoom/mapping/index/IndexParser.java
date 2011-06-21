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

import com.google.common.collect.Lists;

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
		
		Indexed indexedAnn = field.getAnnotation(Indexed.class);
		IndexedInGroup indexedInGroupAnn = field.getAnnotation(IndexedInGroup.class);
		IndexedInGroups indexedInGroupsAnn = field.getAnnotation(IndexedInGroups.class);
	
		if ((indexedAnn != null) && (indexedInGroupAnn != null))
			throw new MappingException(indexContext.getEntityClass(), "Field " + field + " is indexed and indexedInGroup");
		if ((indexedAnn != null) && (indexedInGroupsAnn != null))
			throw new MappingException(indexContext.getEntityClass(), "Field " + field + " is indexed and indexedInGroups");
		if ((indexedInGroupAnn != null) && (indexedInGroupsAnn != null))
			throw new MappingException(indexContext.getEntityClass(), "Field " + field
					+ " is indexedInGroup and indexedInGroups (use indexedInGroups only)");
		if ((indexedInGroupsAnn != null) && (indexedInGroupsAnn.value().length == 0))
			throw new MappingException(indexContext.getEntityClass(), "Field " + field
					+ " is indexedInGroups but in which one? (Annotation value is empty)");
	
		IndexedInGroup[] idxInGAnnList = null;
		if (indexedInGroupAnn != null)
			idxInGAnnList = new IndexedInGroup[] {indexedInGroupAnn};
		if (indexedInGroupsAnn != null)
			idxInGAnnList = indexedInGroupsAnn.value();
	
		if (indexedAnn != null) {
			List<IndexDef> subIndexes = indexContext.getFieldConverter().getIndexes();
			if ((subIndexes != null) && (!subIndexes.isEmpty()))
				throw new MappingException(indexContext.getEntityClass(), "Field " + field + " is indexed, but type has index too");
	
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
							throw new MappingException(indexContext.getEntityClass(), "Field " + field + ", IndexGroup " + groupName + " not defined");
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

}
