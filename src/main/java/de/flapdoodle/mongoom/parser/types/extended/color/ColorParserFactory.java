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

package de.flapdoodle.mongoom.parser.types.extended.color;

import java.awt.Color;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import de.flapdoodle.mongoom.annotations.index.IndexedInGroup;
import de.flapdoodle.mongoom.annotations.index.IndexedInGroups;
import de.flapdoodle.mongoom.mapping.converter.extended.color.ColorConverterOptions;
import de.flapdoodle.mongoom.parser.IFieldType;
import de.flapdoodle.mongoom.parser.IMappedProperty;
import de.flapdoodle.mongoom.parser.IMappingParserContext;
import de.flapdoodle.mongoom.parser.IMapProperties;
import de.flapdoodle.mongoom.parser.IType;
import de.flapdoodle.mongoom.parser.ITypeParser;
import de.flapdoodle.mongoom.parser.ITypeParserFactory;
import de.flapdoodle.mongoom.parser.mapping.Mapping;

public class ColorParserFactory implements ITypeParserFactory {

	@Override
	public ITypeParser getParser(IType type) {
		if (type.getType().isAssignableFrom(Color.class)) {
			return new ColorParser();
		}
		return null;
	}

	static class ColorParser implements ITypeParser {

		@Override
		public void parse(IMappingParserContext mappingParserContext, IMapProperties propertyMapping) {
			ColorConverterOptions colorAnnotation = propertyMapping.getType().getAnnotation(ColorConverterOptions.class);

			IMappedProperty red = propertyMapping.newProperty(new ColorChannelType(ColorChannel.RED));
			IMappedProperty green = propertyMapping.newProperty(new ColorChannelType(ColorChannel.GREEN));
			IMappedProperty blue = propertyMapping.newProperty(new ColorChannelType(ColorChannel.BLUE));

			if (colorAnnotation != null) {
				if (colorAnnotation.red() != null)
					red.setIndexedInGroup(colorAnnotation.red().value());
				if (colorAnnotation.green() != null)
					green.setIndexedInGroup(colorAnnotation.green().value());
				if (colorAnnotation.blue() != null)
					blue.setIndexedInGroup(colorAnnotation.blue().value());
			}
		}
	}

	static enum ColorChannel {
		RED,
		GREEN,
		BLUE;
	}

	static class ColorChannelType implements IFieldType {

		private final ColorChannel _color;

		public ColorChannelType(ColorChannel color) {
			_color = color;
		}

		@Override
		public Class<?> getType() {
			return Integer.class;
		}

		@Override
		public Type getGenericType() {
			return Integer.class;
		}
		
		@Override
		public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
			return null;
		}

		@Override
		public String getName() {
			switch (_color) {
				case BLUE:
					return "blue";
				case GREEN:
					return "green";
				case RED:
					return "red";
			}
			throw new IllegalArgumentException("should not reach this");
		}

		@Override
		public String toString() {
			return "ColorChannel(" + _color + ")";
		}
	}

}
