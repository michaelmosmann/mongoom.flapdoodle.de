package de.flapdoodle.mongoom.testlab.typeinfo;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import de.flapdoodle.mongoom.testlab.ITypeInfo;

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