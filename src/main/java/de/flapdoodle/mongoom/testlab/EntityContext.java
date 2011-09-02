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

package de.flapdoodle.mongoom.testlab;

import java.util.Map;

import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.annotations.Views;
import de.flapdoodle.mongoom.mapping.index.EntityIndexDef;


public class EntityContext<EntityBean> implements IEntityContext{

	private final Class<EntityBean> _entityClass;
	private final Entity _entityAnnotation;
	private final Views _viewsAnnotation;
	private final Map<String, EntityIndexDef> _indexGroupMap;

	public EntityContext(Class<EntityBean> entityClass,Entity entityAnnotation, Views viewsAnnotation, Map<String, EntityIndexDef> indexGroupMap) {
		_entityClass = entityClass;
		_entityAnnotation = entityAnnotation;
		_viewsAnnotation = viewsAnnotation;
		_indexGroupMap = indexGroupMap;
		
	}

}
