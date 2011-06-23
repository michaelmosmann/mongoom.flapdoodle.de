package de.flapdoodle.mongoom.parser;

import de.flapdoodle.mongoom.parser.mapping.EntityMapping;


public interface IEntityMapping extends IMapping {

	EntityMapping newEntity(Class<?> entityClass);

}
