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