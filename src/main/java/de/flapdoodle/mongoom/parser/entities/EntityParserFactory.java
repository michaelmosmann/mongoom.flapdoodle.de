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

package de.flapdoodle.mongoom.parser.entities;

import de.flapdoodle.mongoom.parser.IEntityParser;
import de.flapdoodle.mongoom.parser.IEntityParserFactory;
import de.flapdoodle.mongoom.parser.IType;
import de.flapdoodle.mongoom.parser.ITypeParserFactory;


public class EntityParserFactory implements IEntityParserFactory {

	
	private final ITypeParserFactory _customFactory;

	public EntityParserFactory() {
		_customFactory=null;
	}
	
	public EntityParserFactory(ITypeParserFactory customFactory) {
		_customFactory = customFactory;
	}
	
	@Override
	public IEntityParser getParser(IType entityClass) {
		return new EntityParser(_customFactory);
	}

	@Override
	public ITypeParserFactory getTypeParserFactory() {
		return _customFactory;
	}
}
