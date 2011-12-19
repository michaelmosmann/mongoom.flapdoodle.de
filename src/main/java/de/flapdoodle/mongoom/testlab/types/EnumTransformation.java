package de.flapdoodle.mongoom.testlab.types;

import java.util.Set;

import org.bson.types.ObjectId;

import de.flapdoodle.mongoom.testlab.ITransformation;
import de.flapdoodle.mongoom.testlab.properties.TypedPropertyName;
import de.flapdoodle.mongoom.types.Reference;


public class EnumTransformation<E extends Enum<E>> implements ITransformation<E,String> {

	private final Class<E> _type;

	public EnumTransformation(Class<E> type) {
		_type = type;
	}
	
	@Override
	public E asEntity(String object) {
		return object!=null ? Enum.valueOf(_type, object) : null;
	}
	
	public String asObject(E value) {
		return value!=null ? value.name() : null;
	}
	
	@Override
	public <Source> ITransformation<Source, ?> propertyTransformation(TypedPropertyName<Source> property) {
		return null;
	}

	@Override
	public ITransformation<?, ?> propertyTransformation(String property) {
		return null;
	}

	@Override
	public Set<TypedPropertyName<?>> properties() {
		return null;
	};
}
