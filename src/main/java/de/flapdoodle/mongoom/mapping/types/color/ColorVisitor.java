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

package de.flapdoodle.mongoom.mapping.types.color;

import java.awt.Color;

import com.mongodb.DBObject;

import de.flapdoodle.mongoom.annotations.index.IndexedInGroup;
import de.flapdoodle.mongoom.mapping.ITransformation;
import de.flapdoodle.mongoom.mapping.ITypeInfo;
import de.flapdoodle.mongoom.mapping.ITypeVisitor;
import de.flapdoodle.mongoom.mapping.context.IMappingContext;
import de.flapdoodle.mongoom.mapping.context.IPropertyContext;
import de.flapdoodle.mongoom.mapping.properties.IAnnotated;
import de.flapdoodle.mongoom.mapping.properties.Property;
import de.flapdoodle.mongoom.mapping.properties.PropertyName;


public class ColorVisitor implements ITypeVisitor<Color, DBObject>{

	@Override
	public ITransformation<Color, DBObject> transformation(IMappingContext mappingContext,
			IPropertyContext<?> propertyContext, ITypeInfo field) {
		if (field instanceof IAnnotated) {
			ColorConverterOptions options=((IAnnotated) field).getAnnotation(ColorConverterOptions.class);
			if (options!=null) {
				addIndex(propertyContext, PropertyName.with("r",Integer.class), options.red());
				addIndex(propertyContext, PropertyName.with("g",Integer.class), options.green());
				addIndex(propertyContext, PropertyName.with("b",Integer.class), options.blue());
				addIndex(propertyContext, PropertyName.with("a",Integer.class), options.alpha());
			}
		}
		return new ColorTransformation();
	}

	private void addIndex(IPropertyContext<?> propertyContext, PropertyName<Integer> channelName, IndexedInGroup[] channelIndex) {
		if (channelIndex!=null) {
			IPropertyContext<Integer> rContext = propertyContext.contextFor(Property.of(channelName, Integer.class));
			for (IndexedInGroup ig : channelIndex) {
				rContext.propertyIndex().addIndexedInGroup(ig);
			}
		}
	}

}
