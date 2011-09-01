package de.flapdoodle.mongoom.testlab;

import java.lang.reflect.Type;


public class Property<T> {
	private final String _name;
	private final Class<T> _type;
	private final Type _genericType;

	public Property(String name, Class<T> type, Type genericType) {
		_name = name;
		_type = type;
		_genericType = genericType;
	}
}