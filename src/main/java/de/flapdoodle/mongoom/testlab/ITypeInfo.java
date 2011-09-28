package de.flapdoodle.mongoom.testlab;

import java.lang.reflect.Field;
import java.lang.reflect.Type;


public interface ITypeInfo {

	Class<?> getType();

	Class<?> getDeclaringClass();

	Type getGenericType();

}
