package de.flapdoodle.mongoom.testlab;

import de.flapdoodle.mongoom.exceptions.MappingException;


public abstract class AbstractVisitor {

	protected void error(Class<?> type, String message) {
		throw new MappingException(type, message);
	}

}
