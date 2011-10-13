package de.flapdoodle.mongoom.testlab.types;

import java.util.Map;

import com.google.common.collect.Maps;

import de.flapdoodle.mongoom.testlab.IPropertyContext;
import de.flapdoodle.mongoom.testlab.ITransformation;
import de.flapdoodle.mongoom.testlab.Property;

class PojoContext<T> implements IPropertyContext<T> {

	private final Map<Property<?>, ITransformation<?, ?>> propertyTransformation = Maps.newLinkedHashMap();
	private final Class<T> _beanClass;

	public PojoContext(Class<T> entityClass) {
		_beanClass = entityClass;
	}
	
	@Override
	public <S> IPropertyContext<S> contextFor(Property<S> of) {
		return null;
	}

	@Override
	public <S> void setTransformation(Property<S> property, ITransformation<S, ?> transformation) {
		propertyTransformation.put(property, transformation);
	}

	public Map<Property<?>, ITransformation<?, ?>> getPropertyTransformation() {
		return propertyTransformation;
	}

	public Class<T> getBeanClass() {
		return _beanClass;
	}
	
}