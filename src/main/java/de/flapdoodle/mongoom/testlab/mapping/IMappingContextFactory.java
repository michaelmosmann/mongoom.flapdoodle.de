package de.flapdoodle.mongoom.testlab.mapping;


public interface IMappingContextFactory<T extends IMappingContext> {
	T newContext();
}
