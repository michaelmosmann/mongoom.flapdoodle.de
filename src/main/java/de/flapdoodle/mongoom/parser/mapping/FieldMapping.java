package de.flapdoodle.mongoom.parser.mapping;

import de.flapdoodle.mongoom.parser.IMapping;
import de.flapdoodle.mongoom.parser.IPropertyMapping;

public class FieldMapping implements IPropertyMapping {

	private final String _name;

	public FieldMapping(String name) {
		_name = name;
	}

	@Override
	public IPropertyMapping newProperty(String name) {
		return null;
	}
}
