package de.flapdoodle.mongoom.testlab.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;

import com.google.common.collect.Lists;
import com.mongodb.BasicDBList;

import de.flapdoodle.mongoom.testlab.ITransformation;
import de.flapdoodle.mongoom.testlab.Property;
import de.flapdoodle.mongoom.types.Reference;

public class SetTransformation<Bean, Mapped> implements ITransformation<Set<Bean>, List<Mapped>> {

	private final Class<Bean> _collectionType;
	private final ITransformation<Bean, Mapped> _transformation;

	public SetTransformation(Class<Bean> collectionType, ITransformation<Bean, Mapped> transformation) {
		_collectionType = collectionType;
		_transformation = transformation;
	}

	@Override
	public List<Mapped> asObject(Set<Bean> value) {
		if (value == null) return null;
		ArrayList<Mapped> ret = Lists.newArrayList();
		for (Bean b : value) {
			ret.add(_transformation.asObject(b));
		}
		return ret;
	}

	@Override
	public Set<Bean> asEntity(List<Mapped> object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <Source> ITransformation<Source, ?> propertyTransformation(Property<Source> property) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Property<?>> properties() {
		// TODO Auto-generated method stub
		return null;
	}

}
