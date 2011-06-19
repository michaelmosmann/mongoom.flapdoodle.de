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

package de.flapdoodle.mongoom.mapping.converter.extended.color;

import java.awt.Color;
import java.lang.reflect.Type;

import de.flapdoodle.mongoom.mapping.ITypeConverter;
import de.flapdoodle.mongoom.mapping.Mapper;
import de.flapdoodle.mongoom.mapping.MappingContext;
import de.flapdoodle.mongoom.mapping.converter.ITypeConverterFactory;
import de.flapdoodle.mongoom.mapping.converter.annotations.IAnnotated;


public class ColorConverterFactory implements ITypeConverterFactory<Color> {

	@Override
	public ITypeConverter<? extends Color> converter(Mapper mapper, MappingContext context, Class<Color> type,
			Type genericType, IAnnotated annotations) {
		return new ColorConverter(annotations);
	}
	
}