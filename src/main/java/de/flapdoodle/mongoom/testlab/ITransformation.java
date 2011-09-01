package de.flapdoodle.mongoom.testlab;

import java.util.Set;

public interface ITransformation<Bean,Mapped> {

	Mapped asObject(Bean value);

	Bean asEntity(Mapped object);
	
	<Source> ITransformation<Source,?> propertyTransformation(Property<Source> property);
	
	Set<Property<?>> properties();
}
