package de.flapdoodle.mongoom.testlab;

import java.lang.reflect.Field;


public interface IMappingContext {

	<Type> ITypeVisitor<Type, ?> getVisitor(Class<?> containerType, Field field);
}
