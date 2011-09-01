package de.flapdoodle.mongoom.testlab;

import com.mongodb.DBObject;


public interface ITypeVisitor<Type,Mapped> {
	ITransformation<Type, Mapped> transformation(IEntityContext<?> entityContext, Class<Type> type);
}
