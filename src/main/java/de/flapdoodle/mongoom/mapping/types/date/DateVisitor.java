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

package de.flapdoodle.mongoom.mapping.types.date;

import java.awt.Color;
import java.util.Date;

import com.mongodb.DBObject;

import de.flapdoodle.mongoom.annotations.index.Indexed;
import de.flapdoodle.mongoom.annotations.index.IndexedInGroup;
import de.flapdoodle.mongoom.annotations.index.IndexedInGroups;
import de.flapdoodle.mongoom.mapping.ITransformation;
import de.flapdoodle.mongoom.mapping.ITypeInfo;
import de.flapdoodle.mongoom.mapping.ITypeVisitor;
import de.flapdoodle.mongoom.mapping.context.IMappingContext;
import de.flapdoodle.mongoom.mapping.context.IPropertyContext;
import de.flapdoodle.mongoom.mapping.naming.PropertyName;
import de.flapdoodle.mongoom.mapping.properties.IAnnotated;
import de.flapdoodle.mongoom.mapping.properties.Property;
import de.flapdoodle.mongoom.mapping.types.color.ColorConverterOptions;
import de.flapdoodle.mongoom.mapping.types.color.ColorTransformation;


public class DateVisitor implements ITypeVisitor<Date, DBObject>{

	@Override
	public ITransformation<Date, DBObject> transformation(IMappingContext mappingContext,
			IPropertyContext<?> propertyContext, ITypeInfo field) {
		if (field instanceof IAnnotated) {
			IAnnotated annotated = (IAnnotated) field;
			DateMappingOptions options=annotated.getAnnotation(DateMappingOptions.class);
			if (options!=null) {
				addIndex(propertyContext, PropertyName.with("year","y",Integer.class), Integer.class, options.year());
				addIndex(propertyContext, PropertyName.with("month","m",Integer.class), Integer.class, options.month());
				addIndex(propertyContext, PropertyName.with("day","d",Integer.class), Integer.class, options.day());
				addIndex(propertyContext, PropertyName.with("hour","H",Integer.class), Integer.class, options.hour());
				addIndex(propertyContext, PropertyName.with("minute","M",Integer.class), Integer.class, options.minute());
				addIndex(propertyContext, PropertyName.with("second","s",Integer.class), Integer.class, options.second());
			}
			IndexedInGroup iig=annotated.getAnnotation(IndexedInGroup.class);
			if (iig!=null) {
				addIndex(propertyContext, PropertyName.with("time","t",Date.class), Date.class, iig);
			}
			IndexedInGroups iigs=annotated.getAnnotation(IndexedInGroups.class);
			if (iigs!=null) {
				addIndex(propertyContext, PropertyName.with("time","t",Date.class), Date.class, iigs.value());
			}
			Indexed ii=annotated.getAnnotation(Indexed.class);
			if (ii!=null) {
				IPropertyContext<Date> rContext = propertyContext.contextFor(Property.of(PropertyName.with("time","t",Date.class), Date.class));
				rContext.propertyIndex().setIndexed(ii);
			}
		}
		return new DateTransformation();
	}

	private <T> void addIndex(IPropertyContext<?> propertyContext, PropertyName channelName, Class<T> type, IndexedInGroup... channelIndex) {
		if (channelIndex!=null) {
			IPropertyContext<T> rContext = propertyContext.contextFor(Property.of(channelName, type));
			for (IndexedInGroup ig : channelIndex) {
				rContext.propertyIndex().addIndexedInGroup(ig);
			}
		}
	}

}
