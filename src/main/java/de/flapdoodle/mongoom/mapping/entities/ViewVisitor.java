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

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mongodb.DBObject;

import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.annotations.Views;
import de.flapdoodle.mongoom.mapping.AbstractClassFieldVisitor;
import de.flapdoodle.mongoom.mapping.ITransformation;
import de.flapdoodle.mongoom.mapping.ITypeInfo;
import de.flapdoodle.mongoom.mapping.IViewTransformation;
import de.flapdoodle.mongoom.mapping.IViewVisitor;
import de.flapdoodle.mongoom.mapping.context.IMappingContext;
import de.flapdoodle.mongoom.mapping.converter.reflection.ClassInformation;
import de.flapdoodle.mongoom.mapping.index.EntityIndexDef;
import de.flapdoodle.mongoom.mapping.index.IndexParser;
import de.flapdoodle.mongoom.mapping.properties.IPropertyField;
import de.flapdoodle.mongoom.mapping.properties.Property;
import de.flapdoodle.mongoom.mapping.properties.PropertyName;
import de.flapdoodle.mongoom.mapping.properties.TypedPropertyName;
import de.flapdoodle.mongoom.mapping.typeinfo.TypeInfo;

public class ViewVisitor<ViewBean> extends AbstractClassFieldVisitor<ViewBean, DBObject> implements
		IViewVisitor<ViewBean> {

	private final EntityContext<?> entityContext;

	public ViewVisitor(EntityContext<?> entityContext) {
		this.entityContext = entityContext;
	}

	@Override
	public IViewTransformation<ViewBean, DBObject> transformation(IMappingContext mappingContext, Class<ViewBean> viewClass) {
		Entity entityAnnotation = viewClass.getAnnotation(Entity.class);
		if (entityAnnotation != null) {
			error(viewClass, "Has " + Entity.class + " Annotation");
		}
		Views viewsAnnotation = viewClass.getAnnotation(Views.class);
		if (viewsAnnotation != null) {
			error(viewClass, "Has " + Views.class + " Annotation");
		}
		Map<String, EntityIndexDef> indexGroupMap = IndexParser.getIndexGroupMap(viewClass);
		if ((indexGroupMap != null) && (!indexGroupMap.isEmpty())) {
			error(viewClass, "Has Index Annotations");
		}

		ViewContext<ViewBean> viewContext = new ViewContext<ViewBean>(viewClass);
//		parseProperties(mappingContext, entityContext, TypeInfo.ofClass(viewClass));
		Class<ViewBean> entityClass = viewClass;
		List<Field> fields = ClassInformation.getFields(entityClass);
		
		ITypeInfo typeInfo = TypeInfo.ofClass(viewClass);
		for (Field field : fields) {
			ITypeInfo fieldInfo = TypeInfo.of(typeInfo,field);
			IPropertyField<?> property = Property.of(mappingContext.naming().name(field, PropertyName.empty()),field);
			List<String> parts=Property.split(property.name().getMapped());
			ITransformation transformation=getTransformation(this.entityContext,parts);
			viewContext.setTransformation(Property.of(property.name(), field), transformation);
		}

//		for (TypedPropertyName<?> props : entityContext.getPropertyTransformations().typedPropertyNames()) {
//			IProperty<?> prop = entityContext.getPropertyTransformations().getProperty(props);
//			IAnnotated annotated = prop.annotated();
//			if (annotated != null) {
//				Version version = annotated.getAnnotation(Version.class);
//				if (version != null) {
//					error(viewClass, "Version annotated: " + props);
//				}
//			}
//		}

		return new ViewTransformation<ViewBean>(viewContext);
	}

	private static ITransformation<?, ?> getTransformation(EntityContext<?> entityContext, List<String> parts) {
		String first=parts.get(0);
		List<String> left = parts.subList(1, parts.size());
		IPropertyTransformations propertyTransformations = entityContext.getPropertyTransformations();
		ITransformation<?, ?> ret = propertyTransformations.get(getMappedPropertyName(propertyTransformations, first));
		ret=getTransformation(ret,left);
		return ret;
	}

	private static PropertyName<?> getMappedPropertyName(IPropertyTransformations propertyTransformations, String first) {
		Set<PropertyName<?>> propertyNames = propertyTransformations.propertyNames();
		return getMappedPropertyName(first, propertyNames);
	}

	private static PropertyName<?> getMappedPropertyName(String first, Set<PropertyName<?>> propertyNames) {
		for (PropertyName<?> p : propertyNames) {
			if (p.getMapped().equals(first)) return p;
		}
		return null;
	}

	private static ITransformation<?, ?> getTransformation(ITransformation<?, ?> ret, List<String> parts) {
		if (!parts.isEmpty()) {
			String first=parts.get(0);
			List<String> left = parts.subList(1, parts.size());
			PropertyName pn=getMappedPropertyName(first, ret.properties());
			ret=ret.propertyTransformation(pn);
			getTransformation(ret, left);
		}
		return ret;
	}
}
