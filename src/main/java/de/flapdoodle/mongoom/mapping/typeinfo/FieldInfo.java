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

package de.flapdoodle.mongoom.mapping.typeinfo;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import de.flapdoodle.mongoom.mapping.ITypeInfo;

@Deprecated class FieldInfo implements ITypeInfo {
	
	private final Field _field;

	public FieldInfo(Field field) {
		_field = field;
	}
	
	@Override
	public Class<?> getType() {
		return _field.getType();
	}
	
	@Override
	public Class<?> getDeclaringClass() {
		return _field.getDeclaringClass();
	}
	
	@Override
	public Type getGenericType() {
		return _field.getGenericType();
	}
	
	@Override
	public String toString() {
		return "FieldInfo ("+_field+"(type: "+_field.getType()+",genericType: "+_field.getGenericType()+")";
	}
}