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

package de.flapdoodle.mongoom.testlab.typeinfo;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;

import de.flapdoodle.mongoom.mapping.converter.generics.TypeExtractor;
import de.flapdoodle.mongoom.testlab.ITypeInfo;

class FieldOfClassInfo implements ITypeInfo {
		
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
//			_logger.severe("\n--------------------\nField: "+_field+"\nGen: "+genericType+"\nGenClass: "+genericType.getClass()+"\n------------------\n");
			if (genericType instanceof TypeVariable) {
				Map<Type, Type> typeArgumentMap = TypeExtractor.getTypeArgumentMap(_clazz.getGenericType());
//				_logger.severe("Map: "+typeArgumentMap);
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