package de.flapdoodle.mongoom.testlab.types;

import java.util.Collections;
import java.util.Map;

import com.google.common.collect.Maps;

import de.flapdoodle.mongoom.testlab.IPropertyContext;
import de.flapdoodle.mongoom.testlab.ITransformation;
import de.flapdoodle.mongoom.testlab.Property;
import de.flapdoodle.mongoom.testlab.PropertyContext;

class PojoContext<T> extends PropertyContext<T> {

	private final Class<T> _beanClass;

	public PojoContext(IPropertyContext<?> parentContext, Class<T> entityClass) {
		super(parentContext);
		_beanClass = entityClass;
	}

	@Override
	public <S> IPropertyContext<S> contextFor(Property<S> of) {
		return null;
	}

	public Class<T> getBeanClass() {
		return _beanClass;
	}

}
