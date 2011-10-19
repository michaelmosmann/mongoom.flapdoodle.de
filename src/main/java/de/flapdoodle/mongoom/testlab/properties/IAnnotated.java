package de.flapdoodle.mongoom.testlab.properties;

import java.lang.annotation.Annotation;


public interface IAnnotated {

	<T extends Annotation> T getAnnotation(Class<T> annotationClass);

}
