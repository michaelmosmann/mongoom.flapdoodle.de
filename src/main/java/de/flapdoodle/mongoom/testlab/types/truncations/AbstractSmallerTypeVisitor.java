package de.flapdoodle.mongoom.testlab.types.truncations;

import java.util.Arrays;
import java.util.logging.Logger;

import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.logging.LogConfig;
import de.flapdoodle.mongoom.testlab.AbstractVisitor;
import de.flapdoodle.mongoom.testlab.ITransformation;
import de.flapdoodle.mongoom.testlab.ITypeInfo;
import de.flapdoodle.mongoom.testlab.ITypeVisitor;
import de.flapdoodle.mongoom.testlab.mapping.IMappingContext;
import de.flapdoodle.mongoom.testlab.mapping.IPropertyContext;


public abstract class AbstractSmallerTypeVisitor<S,B> extends AbstractVisitor implements ITypeVisitor<S,B> {
	
	public static Logger _logger=LogConfig.getLogger(AbstractSmallerTypeVisitor.class);
	
	private final Class<?>[] _types;
	private final Class<B> _bigType;

	protected AbstractSmallerTypeVisitor(Class<B> bigType, Class<?>... types) {
		_bigType = bigType;
		_types = types;
	}
	
	@Override
	public ITransformation<S, B> transformation(IMappingContext mappingContext,
			IPropertyContext<?> propertyContext, ITypeInfo field) {
		if (isType(field)) {
			_logger.warning("Data truncation may occur for "+field+", better use this type: "+_bigType);
			return newTransformation();
		}
		throw new MappingException(field.getDeclaringClass(), "Type does not match: " + Arrays.asList(_types)+"!="+field.getType());
	}

	protected abstract ITransformation<S, B> newTransformation();

	private boolean isType(ITypeInfo field) {
		for (Class<?> type : _types) {
			if (type.isAssignableFrom(field.getType())) return true;
		}
		return false;
	}

}
