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

import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.annotations.Version;
import de.flapdoodle.mongoom.annotations.Views;
import de.flapdoodle.mongoom.mapping.callbacks.Callbacks;
import de.flapdoodle.mongoom.mapping.callbacks.IEntityReadCallback;
import de.flapdoodle.mongoom.mapping.index.EntityIndexDef;
import de.flapdoodle.mongoom.mapping.index.IndexParser;
import de.flapdoodle.mongoom.testlab.AbstractClassFieldVisitor;
import de.flapdoodle.mongoom.testlab.ITransformation;
import de.flapdoodle.mongoom.testlab.IViewTransformation;
import de.flapdoodle.mongoom.testlab.IViewVisitor;
import de.flapdoodle.mongoom.testlab.mapping.IMappingContext;
import de.flapdoodle.mongoom.testlab.properties.IAnnotated;
import de.flapdoodle.mongoom.testlab.properties.Property;
import de.flapdoodle.mongoom.testlab.properties.PropertyName;
import de.flapdoodle.mongoom.testlab.typeinfo.TypeInfo;

public class ViewVisitor<ViewBean> extends AbstractClassFieldVisitor<ViewBean, DBObject> implements
		IViewVisitor<ViewBean> {

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

		ViewContext<ViewBean> entityContext = new ViewContext<ViewBean>(viewClass);
		parseProperties(mappingContext, entityContext, TypeInfo.ofClass(viewClass));

		for (PropertyName<?> props : entityContext.getPropertyTransformations().propertyNames()) {
			Property<?> prop = entityContext.getPropertyTransformations().getProperty(props);
			IAnnotated annotated = prop.annotated();
			if (annotated != null) {
				Version version = annotated.getAnnotation(Version.class);
				if (version != null) {
					error(viewClass, "Version annotated: " + props);
				}
			}
		}

		return new ViewTransformation<ViewBean>(entityContext);
	}
}
