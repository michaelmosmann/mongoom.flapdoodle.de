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

package de.flapdoodle.mongoom.mapping.entities;

import java.util.Map;

import com.mongodb.DBObject;

import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.annotations.Id;
import de.flapdoodle.mongoom.annotations.Version;
import de.flapdoodle.mongoom.annotations.Views;
import de.flapdoodle.mongoom.mapping.AbstractClassFieldVisitor;
import de.flapdoodle.mongoom.mapping.IEntityTransformation;
import de.flapdoodle.mongoom.mapping.IEntityVisitor;
import de.flapdoodle.mongoom.mapping.IViewTransformation;
import de.flapdoodle.mongoom.mapping.callbacks.Callbacks;
import de.flapdoodle.mongoom.mapping.callbacks.IEntityReadCallback;
import de.flapdoodle.mongoom.mapping.callbacks.IEntityWriteCallback;
import de.flapdoodle.mongoom.mapping.context.IMappingContext;
import de.flapdoodle.mongoom.mapping.index.EntityIndexDef;
import de.flapdoodle.mongoom.mapping.index.IndexParser;
import de.flapdoodle.mongoom.mapping.naming.PropertyName;
import de.flapdoodle.mongoom.mapping.properties.IAnnotated;
import de.flapdoodle.mongoom.mapping.properties.IProperty;
import de.flapdoodle.mongoom.mapping.properties.IPropertyField;
import de.flapdoodle.mongoom.mapping.reflection.IResolvedType;
import de.flapdoodle.mongoom.mapping.reflection.ITypeResolver;
import de.flapdoodle.mongoom.mapping.typeinfo.TypeInfo;
import de.flapdoodle.mongoom.mapping.versions.IVersionFactory;

public class EntityVisitor<EntityBean> extends AbstractClassFieldVisitor<EntityBean, DBObject> implements
		IEntityVisitor<EntityBean> {

	@Override
	public IEntityTransformation<EntityBean> transformation(IMappingContext mappingContext, Class<EntityBean> entityClass) {
		
		if (false) {
			ITypeResolver typeResolver = mappingContext.typeResolver();
			IResolvedType resolvedType = typeResolver.resolve(entityClass);
			{
				Entity entityAnnotation = resolvedType.getAnnotation(Entity.class);
				if (entityAnnotation == null) {
					error(entityClass, "Missing " + Entity.class + " Annotation");
				}
				Views viewsAnnotation = entityClass.getAnnotation(Views.class);
			}
		}
		
//		memberResolver.resolve(entityClass).
		
		Entity entityAnnotation = entityClass.getAnnotation(Entity.class);
		if (entityAnnotation == null) {
			error(entityClass, "Missing " + Entity.class + " Annotation");
		}
		Views viewsAnnotation = entityClass.getAnnotation(Views.class);

		Map<String, EntityIndexDef> indexGroupMap = IndexParser.getIndexGroupMap(entityClass);

		EntityContext<EntityBean> entityContext = new EntityContext<EntityBean>(entityClass, entityAnnotation,
				viewsAnnotation, indexGroupMap);
		parseProperties(mappingContext, entityContext, TypeInfo.ofClass(entityClass));

		for (PropertyName props : entityContext.getPropertyTransformations().propertyNames()) {
			IProperty<?> prop = entityContext.getPropertyTransformations().getProperty(props);
			IAnnotated annotated = prop.annotated();
			if (annotated != null) {
				Version version = annotated.getAnnotation(Version.class);
				if (version != null) {
					if (!(prop instanceof IPropertyField)) error(entityClass, "Property is not PropertyField: "+prop);
					IPropertyField<?> propertyField=(IPropertyField<?>) prop;
					
					IVersionFactory<?> versionFactory = mappingContext.versionFactory(TypeInfo.of(TypeInfo.ofClass(entityClass),
							propertyField.getField()));
					if (versionFactory == null) {
						error(entityClass, "Version annotated but no Factory found: " + props);
					} else {
						entityContext.setVersionFactory(propertyField, versionFactory);
					}
				}
				Id id = annotated.getAnnotation(Id.class);
				if (id != null) {
					if (!(prop instanceof IPropertyField)) error(entityClass, "Property is not PropertyField: "+prop);
					IPropertyField<?> propertyField=(IPropertyField<?>) prop;
					entityContext.setId(propertyField, entityContext.getPropertyTransformations().get(props));
				}
			}
		}

		if (viewsAnnotation != null) {
			for (Class<?> viewType : viewsAnnotation.value()) {
				IViewTransformation transformation = new ViewVisitor(entityContext).transformation(mappingContext, viewType);
				entityContext.setViewTransformation(viewType, transformation);
			}
		}

		entityContext.setReadCallback(Callbacks.newInstance(entityClass, entityAnnotation.onRead(),
				IEntityReadCallback.class));
		entityContext.setWriteCallback(Callbacks.newInstance(entityClass, entityAnnotation.onWrite(),
				IEntityWriteCallback.class));

		return new EntityTransformation<EntityBean>(entityContext);
	}

}
