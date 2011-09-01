package de.flapdoodle.mongoom.testlab;

import java.util.Set;

import com.mongodb.DBObject;


public class EntityTransformation<Bean> implements ITransformation<Bean, DBObject> {

	@Override
	public DBObject asObject(Bean value) {
		
		return null;
	}

	@Override
	public Bean asEntity(DBObject object) {
		return null;
	}

	@Override
	public <Source> ITransformation<Source, ?> propertyTransformation(Property<Source> property) {
		return null;
	}

	@Override
	public Set<Property<?>> properties() {
		return null;
	}

}
