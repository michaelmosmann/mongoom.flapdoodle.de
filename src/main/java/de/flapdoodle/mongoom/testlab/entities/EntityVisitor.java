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

package de.flapdoodle.mongoom.testlab.entities;

import java.util.Map;

import com.mongodb.DBObject;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Annotated;

import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.annotations.Id;
import de.flapdoodle.mongoom.annotations.Version;
import de.flapdoodle.mongoom.annotations.Views;
import de.flapdoodle.mongoom.mapping.index.EntityIndexDef;
import de.flapdoodle.mongoom.mapping.index.IndexParser;
import de.flapdoodle.mongoom.testlab.AbstractClassFieldVisitor;
import de.flapdoodle.mongoom.testlab.IEntityTransformation;
import de.flapdoodle.mongoom.testlab.IEntityVisitor;
import de.flapdoodle.mongoom.testlab.ITransformation;
import de.flapdoodle.mongoom.testlab.IViewTransformation;
import de.flapdoodle.mongoom.testlab.mapping.IMappingContext;
import de.flapdoodle.mongoom.testlab.properties.IAnnotated;
import de.flapdoodle.mongoom.testlab.properties.Property;
import de.flapdoodle.mongoom.testlab.properties.PropertyName;
import de.flapdoodle.mongoom.testlab.typeinfo.TypeInfo;
import de.flapdoodle.mongoom.testlab.versions.IVersionFactory;


public class EntityVisitor<EntityBean> extends AbstractClassFieldVisitor<EntityBean,DBObject> implements IEntityVisitor<EntityBean>{

	@Override
	public IEntityTransformation<EntityBean> transformation(IMappingContext mappingContext, Class<EntityBean> entityClass) {
		Entity entityAnnotation = entityClass.getAnnotation(Entity.class);
		if (entityAnnotation == null) {
			error(entityClass, "Missing " + Entity.class + " Annotation");
		}
		Views viewsAnnotation = entityClass.getAnnotation(Views.class);
		
		Map<String, EntityIndexDef> indexGroupMap = IndexParser.getIndexGroupMap(entityClass);

		EntityContext<EntityBean> entityContext = new EntityContext<EntityBean>(entityClass,entityAnnotation,viewsAnnotation,indexGroupMap);
		parseProperties(mappingContext, entityContext,TypeInfo.ofClass(entityClass));
		
		for (PropertyName<?> props : entityContext.getPropertyTransformations().propertyNames()) {
			Property<?> prop = entityContext.getPropertyTransformations().getProperty(props);
			IAnnotated annotated = prop.annotated();
			if (annotated!=null) {
				Version version = annotated.getAnnotation(Version.class);
				if (version!=null) {
					IVersionFactory<?> versionFactory = mappingContext.versionFactory(TypeInfo.of(TypeInfo.ofClass(entityClass), prop.getField()));
					if (versionFactory==null) {
						error(entityClass,"Version annotated but no Factory found: "+props);
					} else {
						entityContext.setVersionFactory(prop,versionFactory);
					}
				}
				Id id=annotated.getAnnotation(Id.class);
				if (id!=null) {
					entityContext.setId(prop,entityContext.getPropertyTransformations().get(props));
				}
			}
		}
		
		if (viewsAnnotation!=null) {
			for (Class<?> viewType : viewsAnnotation.value()) {
				IViewTransformation transformation = new ViewVisitor().transformation(mappingContext, viewType);
				entityContext.setViewTransformation(viewType, transformation);
			}
		}
		
		return new EntityTransformation<EntityBean>(entityContext);
	}


}
