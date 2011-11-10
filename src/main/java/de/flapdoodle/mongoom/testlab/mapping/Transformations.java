package de.flapdoodle.mongoom.testlab.mapping;

import java.util.List;
import java.util.Map;

import com.google.inject.internal.Maps;
import com.mongodb.DBObject;

import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.testlab.IEntityTransformation;
import de.flapdoodle.mongoom.testlab.ITransformation;
import de.flapdoodle.mongoom.testlab.entities.EntityVisitor;

public class Transformations {

	Map<Class<?>, IEntityTransformation<?>> _transformations = Maps.newHashMap();
	private final IMappingContextFactory<?> _contextFactory;
	private final List<Class<?>> _entityTypes;

	public Transformations(IMappingContextFactory<?> contextFactory, List<Class<?>> entityTypes) {
		_contextFactory = contextFactory;
		_entityTypes = entityTypes;
		for (Class<?> type : _entityTypes) {
			map(type);
		}
	}

	protected <T> void map(Class<T> entityType) {
		IMappingContext mappingContext = _contextFactory.newContext();
		EntityVisitor<T> entityVisitor = new EntityVisitor<T>();
		IEntityTransformation<T> transformation = entityVisitor.transformation(mappingContext, entityType);
		if (_transformations.put(entityType, transformation) != null) {
			throw new MappingException(entityType, "allready mapped");
		}
	}

	public <T> IEntityTransformation<T> transformation(Class<T> type) {
		return (IEntityTransformation<T>) _transformations.get(type);
	}
}
