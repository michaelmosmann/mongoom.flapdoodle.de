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

import com.mongodb.DBObject;

import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.annotations.Views;
import de.flapdoodle.mongoom.mapping.index.EntityIndexDef;
import de.flapdoodle.mongoom.mapping.index.IndexParser;


public class EntityVisitor<EntityBean> extends AbstractClassFieldVisitor<EntityBean,DBObject> implements IEntityVisitor<EntityBean>{

	@Override
	public ITransformation<EntityBean, DBObject> transformation(Class<EntityBean> entityClass) {
		Entity entityAnnotation = entityClass.getAnnotation(Entity.class);
		if (entityAnnotation == null) {
			error(entityClass, "Missing " + Entity.class + " Annotation");
		}
		Views viewsAnnotation = entityClass.getAnnotation(Views.class);
		
		Map<String, EntityIndexDef> indexGroupMap = IndexParser.getIndexGroupMap(entityClass);

		
		
		parseProperties(new EntityContext<EntityBean>(entityClass,entityAnnotation,viewsAnnotation,indexGroupMap),entityClass);
		
		return null;
	}


}
