package de.flapdoodle.mongoom.testlab.types;

import java.lang.reflect.Type;
import java.util.List;

import de.flapdoodle.mongoom.testlab.ITransformation;
import de.flapdoodle.mongoom.testlab.ITypeVisitor;


public class ListVisitor<T,M> extends AbstractCollectionVisitor<List<T>,M> implements ITypeVisitor<List<T>, List<M>>{

	protected ITransformation<List<T>, List<M>> transformation(Type parameterizedClass, ITransformation transformation) {
		return new ListTransformation<T,M>((Class<T>) parameterizedClass,transformation);
	}
}