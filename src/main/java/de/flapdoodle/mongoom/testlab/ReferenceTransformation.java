package de.flapdoodle.mongoom.testlab;

import java.util.Set;

import org.bson.types.ObjectId;

import com.google.common.collect.Sets;

import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.types.Reference;


public class ReferenceTransformation<R> implements ITransformation<Reference<R>,ObjectId> {

	
	private final Class<R> _type;

	public ReferenceTransformation(Class<R> type) {
		_type = type;
	}
	
	@Override
	public ObjectId asObject(Reference<R> value) {
		return value.getId();
	}

	@Override
	public Reference<R> asEntity(ObjectId object) {
		return Reference.getInstance(_type, (ObjectId) object);
	}

	@Override
	public <S> ITransformation<S,?> propertyTransformation(Property<S> property) {
		throw new MappingException(_type,"Reference has no Properties");
	}

	@Override
	public Set<Property<?>> properties() {
		return Sets.newHashSet();
	}
	
}
