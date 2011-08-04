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

package de.flapdoodle.mongoom.parser.types.collections;

import java.util.List;
import java.util.Set;

import de.flapdoodle.mongoom.parser.IType;
import de.flapdoodle.mongoom.parser.ITypeParser;
import de.flapdoodle.mongoom.parser.ITypeParserFactory;


public class CollectionParserFactory implements ITypeParserFactory {

	
	public CollectionParserFactory() {
	}
	
	@Override
	public ITypeParser getParser(IType type) {
		Class<?> collectionType = type.getType();
		if (List.class.isAssignableFrom(collectionType)) {
			return new ListParser();
		}
		if (Set.class.isAssignableFrom(collectionType)) {
			return new SetParser();
		}
		return null;
	}

}
