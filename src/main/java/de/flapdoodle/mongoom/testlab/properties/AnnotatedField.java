package de.flapdoodle.mongoom.testlab.properties;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;


public class AnnotatedField implements IAnnotated {
	
	private final Field _field;

	public AnnotatedField(Field field) {
		_field = field;
	}

	@Override
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		return _field.getAnnotation(annotationClass);
	}
	
}
