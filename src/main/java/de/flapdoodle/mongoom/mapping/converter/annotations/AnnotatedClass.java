package de.flapdoodle.mongoom.mapping.converter.annotations;

import java.lang.annotation.Annotation;

public class AnnotatedClass implements IAnnotated {

	private final Class<?> _clazz;

	public AnnotatedClass(Class<?> clazz) {
		_clazz = clazz;
	}

	@Override
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		return _clazz.getAnnotation(annotationClass);
	}
	
	@Override
	public Class<?> getType() {
		return _clazz;
	}

	@Override
	public String toString() {
		return "AnnotatedClass(" + _clazz + ")";
	}
}
