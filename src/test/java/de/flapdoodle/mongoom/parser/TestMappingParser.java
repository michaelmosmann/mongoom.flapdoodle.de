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

package de.flapdoodle.mongoom.parser;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.google.common.collect.Lists;

import de.flapdoodle.mongoom.examples.mapping.Document;
import de.flapdoodle.mongoom.examples.mapping.complex.ColorDocument;
import de.flapdoodle.mongoom.mapping.IMappingConfig;
import de.flapdoodle.mongoom.mapping.MappingConfig;
import de.flapdoodle.mongoom.mapping.converter.extended.color.ColorConverterFactory;
import de.flapdoodle.mongoom.parser.entities.EntityParserFactory;
import de.flapdoodle.mongoom.parser.types.CustomParserFactory;
import de.flapdoodle.mongoom.parser.types.ObjectParserFactory;
import de.flapdoodle.mongoom.parser.types.RawParserFactory;
import de.flapdoodle.mongoom.parser.types.ReferenceParserFactory;
import de.flapdoodle.mongoom.parser.types.collections.CollectionParserFactory;
import de.flapdoodle.mongoom.parser.types.extended.color.ColorParserFactory;


public class TestMappingParser extends TestCase {

	public void testParser() {
		IMappingConfig mappingConfig = MappingConfig.getDefaults();
		mappingConfig.getConverterFactories().add(new ColorConverterFactory());
		
		ArrayList<Class<?>> classes = Lists.newArrayList();
		classes.add(ColorDocument.class);
		classes.add(Document.class);
		
		List<ITypeParserFactory> factories=Lists.newArrayList();
		factories.add(new ReferenceParserFactory());
		factories.add(new RawParserFactory());
		factories.add(new ColorParserFactory());
		factories.add(new CollectionParserFactory());
		
		MappingParser.map(classes,new EntityParserFactory(new CustomParserFactory(factories)));
	}
}
