package de.flapdoodle.mongoom.parser;

import de.flapdoodle.mongoom.parser.mapping.EntityMapping;

public interface IEntityMapping extends IPropertyMapping {

	void setVersionProperty(String name);

	void setIdProperty(String name);

}
