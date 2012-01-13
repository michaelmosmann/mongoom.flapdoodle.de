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

package de.flapdoodle.mongoom.mapping.types;

import java.util.Set;

import de.flapdoodle.mongoom.mapping.ITransformation;
import de.flapdoodle.mongoom.mapping.properties.PropertyReference;
import de.flapdoodle.mongoom.mapping.types.enums.IEnumStringConverter;

public class EnumTransformation<E extends Enum<E>> extends AbstractPrimitiveTransformation<E, String> {

	private final IEnumStringConverter<E> _converter;

	//	private final Class<E> _type;

	public EnumTransformation(Class<E> type, IEnumStringConverter<E> converter) {
		super(type);
		//		_type = type;
		_converter = converter;
	}

	@Override
	public E asEntity(String object) {
		return _converter.fromString(object);
	}

	public String asObject(E value) {
		return _converter.asString(value);
	}
}
