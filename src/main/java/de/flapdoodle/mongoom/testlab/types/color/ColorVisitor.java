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

package de.flapdoodle.mongoom.testlab.types.color;

import java.awt.Color;

import com.mongodb.DBObject;

import de.flapdoodle.mongoom.annotations.index.IndexedInGroup;
import de.flapdoodle.mongoom.mapping.converter.extended.color.ColorConverterOptions;
import de.flapdoodle.mongoom.testlab.ITransformation;
import de.flapdoodle.mongoom.testlab.ITypeInfo;
import de.flapdoodle.mongoom.testlab.ITypeVisitor;
import de.flapdoodle.mongoom.testlab.mapping.IMappingContext;
import de.flapdoodle.mongoom.testlab.mapping.IPropertyContext;
import de.flapdoodle.mongoom.testlab.properties.IAnnotated;
import de.flapdoodle.mongoom.testlab.properties.IProperty;
import de.flapdoodle.mongoom.testlab.properties.Property;


public class ColorVisitor implements ITypeVisitor<Color, DBObject>{

	@Override
	public ITransformation<Color, DBObject> transformation(IMappingContext mappingContext,
			IPropertyContext<?> propertyContext, ITypeInfo field) {
		if (field instanceof IAnnotated) {
			ColorConverterOptions options=((IAnnotated) field).getAnnotation(ColorConverterOptions.class);
			if (options!=null) {
				IPropertyContext<Integer> rContext = propertyContext.contextFor(Property.of("r", Integer.class));
				for (IndexedInGroup ig : options.red()) {
					rContext.propertyIndex().addIndexedInGroup(ig);
				}
//				rContext.addIndex(options.red());
				// TODO set Index Options on context
			}
		}
		return new ColorTransformation();
	}

}
