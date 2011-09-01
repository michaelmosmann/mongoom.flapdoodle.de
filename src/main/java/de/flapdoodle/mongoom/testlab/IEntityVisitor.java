package de.flapdoodle.mongoom.testlab;

import com.mongodb.DBObject;


public interface IEntityVisitor<EntityType> {
	ITransformation<EntityType, DBObject> transformation(Class<EntityType> entityType);
}
