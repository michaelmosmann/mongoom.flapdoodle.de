package de.flapdoodle.mongoom.parser;

import de.flapdoodle.mongoom.parser.mapping.EntityMapping;


public interface IMapping {

	IEntityMapping newEntity(Class<?> entityClass);

}
