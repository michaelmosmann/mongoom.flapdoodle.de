package de.flapdoodle.mongoom.parser.mapping;

import de.flapdoodle.mongoom.parser.IMapping;


public class EntityMapping implements IMapping{

	private final Class<?> _entityClass;

	public EntityMapping(Class<?> entityClass) {
		_entityClass = entityClass;
	}

}
