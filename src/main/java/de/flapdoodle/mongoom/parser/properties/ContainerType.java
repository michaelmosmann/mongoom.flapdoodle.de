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

package de.flapdoodle.mongoom.parser.properties;

import java.lang.reflect.Field;

import de.flapdoodle.mongoom.parser.IContainerType;


public class ContainerType extends FieldType implements IContainerType {

	private final Class<?> _containerType;

	protected ContainerType(Field field,Class<?> containerType) {
		super(field);
		_containerType = containerType;
	}
	
	@Override
	public Class<?> getContainerType() {
		return _containerType;
	}
	
	public static ContainerType of(Field field,Class<?> containerType) {
		return new ContainerType(field,containerType);
	}

	@Override
	public String toString() {
		return _containerType.toString()+"("+super.toString()+")";
	}
	
}
