package de.flapdoodle.mongoom.testlab;

import java.lang.reflect.Field;
import java.lang.reflect.Type;


public class TypeInfo {

	private TypeInfo() {
		throw new IllegalAccessError("Singleton");
	}
	
	public static ITypeInfo of(Field field) {
		return new FieldInfo(field);
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
		
	}
}
