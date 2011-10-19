package de.flapdoodle.mongoom.testlab.properties;

import java.lang.annotation.Annotation;


public class AnnotatedClass implements IAnnotated {

	
	private final Class<?> _type;

	public AnnotatedClass(Class<?> type) {
		_type = type;
	}
	
	@Override
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		return _type.getAnnotation(annotationClass);
	}

}
