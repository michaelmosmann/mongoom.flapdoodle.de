package de.flapdoodle.mongoom.testlab.types;

import de.flapdoodle.mongoom.testlab.AbstractVisitor;
import de.flapdoodle.mongoom.testlab.ITransformation;
import de.flapdoodle.mongoom.testlab.ITypeInfo;
import de.flapdoodle.mongoom.testlab.ITypeVisitor;
import de.flapdoodle.mongoom.testlab.mapping.IMappingContext;
import de.flapdoodle.mongoom.testlab.mapping.IPropertyContext;


public class EnumVisitor<T extends Enum<T>> extends AbstractVisitor implements ITypeVisitor<T,String> {

	@Override
	public ITransformation<T, String> transformation(IMappingContext mappingContext, IPropertyContext<?> propertyContext,
			ITypeInfo field) {
		return new EnumTransformation<T>((Class<T>) field.getType());
	}
}
