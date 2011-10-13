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

package de.flapdoodle.mongoom.testlab;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;
import java.util.logging.Logger;

import de.flapdoodle.mongoom.logging.LogConfig;
import de.flapdoodle.mongoom.mapping.converter.generics.TypeExtractor;


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
		return new ClassInfo(info.getDeclaringClass(),info.getType());
	}

	public static ITypeInfo ofClass(Class<?> clazz) {
		return new ClassInfo(null,clazz);
	}
	
	static class FieldOfClassInfo implements ITypeInfo {
		
		private final Field _field;
		private final ITypeInfo _clazz;
		
		public FieldOfClassInfo(ITypeInfo clazz, Field field) {
			_clazz = clazz;
			_field = field;
		}
		
		@Override
		public Class<?> getType() {
			Type genericType = _field.getGenericType();
//			Type parameterizedClass = TypeExtractor.getParameterizedClass(_clazz.getType(), genericType,0);
			_logger.severe("\n--------------------\nField: "+_field+"\nGen: "+genericType+"\nGenClass: "+genericType.getClass()+"\n------------------\n");
			if (genericType instanceof TypeVariable) {
				Map<Type, Type> typeArgumentMap = TypeExtractor.getTypeArgumentMap(_clazz.getGenericType());
				_logger.severe("Map: "+typeArgumentMap);
				Type realType = typeArgumentMap.get(genericType);
				if ((realType!=null) && (realType instanceof Class)) return (Class<?>) realType;
			}
//			if (parameterizedClass!=null) return (Class<?>) parameterizedClass;
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
			return "FieldOfClass (clazz: "+_clazz.getType()+",type: "+_clazz.getGenericType()+",Field: "+_field+"(type: "+_field.getType()+",genericType: "+_field.getGenericType()+")";
		}
		
	}
	
	static class FieldInfo implements ITypeInfo {
		
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

	public static ITypeInfo of(Class<?> declaringClass, Type parameterizedClass) {
		return new ClassInfo(declaringClass, parameterizedClass);
	}
	
	static class ClassInfo implements ITypeInfo {

		private final Type _type;
		private final Class<?> _declaringClass;

		public ClassInfo(Class<?> declaringClass, Type clazz) {
			_declaringClass = declaringClass;
			_type = clazz;
		}

		@Override
		public Class<?> getType() {
			if (_type instanceof Class) return (Class<?>) _type;
			return null;
		}

		@Override
		public Class<?> getDeclaringClass() {
			return _declaringClass;
		}

		@Override
		public Type getGenericType() {
			return _type;
		}
		
		@Override
		public String toString() {
			return "ClassInfo ("+_declaringClass+"(type: "+_type+")";
		}
	}
}
