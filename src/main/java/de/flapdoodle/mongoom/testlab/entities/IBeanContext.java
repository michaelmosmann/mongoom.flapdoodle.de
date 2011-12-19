package de.flapdoodle.mongoom.testlab.entities;


public interface IBeanContext<T> {

	IPropertyTransformations getPropertyTransformations();
	
	Class<T> getViewClass();
}
