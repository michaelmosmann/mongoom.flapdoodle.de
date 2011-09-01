package de.flapdoodle.mongoom.testlab;

public class TypeVisitor<Type, Mapped> extends AbstractClassFieldVisitor<Type, Mapped> implements
		ITypeVisitor<Type, Mapped> {

	@Override
	public ITransformation<Type, Mapped> transformation(IEntityContext<?> entityContext, Class<Type> type) {
		parseProperties(entityContext,type);
		return null;
	}

}
