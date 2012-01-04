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
import java.util.logging.Logger;

import de.flapdoodle.mongoom.logging.LogConfig;
import de.flapdoodle.mongoom.mapping.ITypeInfo;
import de.flapdoodle.mongoom.mapping.properties.IAnnotated;


public class TypeInfo {

	private static final Logger _logger = LogConfig.getLogger(TypeInfo.class);
	
	private TypeInfo() {
		throw new IllegalAccessError("Singleton");
	}
	
//	public static ITypeInfo of(Field field) {
//		return new FieldInfo(field);
//	}

	public static ITypeInfo of(ITypeInfo clazz,Field field) {
		return new FieldOfClassInfo(clazz,field);
	}

	public static ITypeInfo ofClass(ITypeInfo info) {
		return new ClassInfo(info.getDeclaringClass(),info.getType(),null);
	}

	public static ITypeInfo ofClass(Class<?> clazz) {
		return new ClassInfo(null,clazz,null);
	}
	
	public static ITypeInfo of(ITypeInfo field, Type parameterizedClass) {
		IAnnotated ia=null;
		if (field instanceof IAnnotated) ia=(IAnnotated) field;
		return new ClassInfo(field.getDeclaringClass(), parameterizedClass,ia);
	}
}
