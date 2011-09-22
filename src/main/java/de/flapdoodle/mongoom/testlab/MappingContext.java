package de.flapdoodle.mongoom.testlab;

import java.lang.reflect.Field;


public class MappingContext implements IMappingContext {
	@Override
	public <Type> ITypeVisitor<Type, ?> getVisitor(Class<?> containerType, Field field) {
		return null;
	}
}
