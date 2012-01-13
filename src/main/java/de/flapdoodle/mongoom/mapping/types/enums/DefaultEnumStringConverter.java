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

package de.flapdoodle.mongoom.mapping.types.enums;


public class DefaultEnumStringConverter<E extends Enum<E>> implements IEnumStringConverter<E> {

	private Class<E> _type;

	public DefaultEnumStringConverter(Class<E> type) {
		_type=type;
	}
	
	@Override
	public E fromString(String value) {
		return value!=null ? Enum.valueOf(_type, value) : null;
	}

	@Override
	public String asString(E enumValue) {
		return enumValue!=null ? enumValue.name() : null;
	}
	
}
