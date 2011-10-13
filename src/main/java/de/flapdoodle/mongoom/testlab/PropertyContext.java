package de.flapdoodle.mongoom.testlab;

import java.util.Collections;
import java.util.Map;

import com.google.common.collect.Maps;

import de.flapdoodle.mongoom.exceptions.MappingException;


public class PropertyContext<T> implements IPropertyContext<T>{

	private final Map<Property<?>, ITransformation<?, ?>> propertyTransformation = Maps.newLinkedHashMap();
	private final IPropertyContext<?> _parentContext;

	public PropertyContext(IPropertyContext<?> parentContext) {
		_parentContext=parentContext;
	}

	@Override
	public <S> IPropertyContext<S> contextFor(Property<S> of) {
		throw new MappingException("should never happen");
	}

	@Override
	public <S> void setTransformation(Property<S> property, ITransformation<S, ?> transformation) {
		propertyTransformation.put(property, transformation);
	}
	
	public Map<Property<?>, ITransformation<?, ?>> getPropertyTransformation() {
		return Collections.unmodifiableMap(propertyTransformation);
	}



}
