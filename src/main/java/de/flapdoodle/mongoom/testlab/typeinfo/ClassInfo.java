package de.flapdoodle.mongoom.testlab.typeinfo;

import java.lang.reflect.Type;

import de.flapdoodle.mongoom.testlab.ITypeInfo;

class ClassInfo implements ITypeInfo {

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