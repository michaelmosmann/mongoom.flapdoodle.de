/**
 * Copyright (C) 2010 Michael Mosmann <michael@mosmann.de>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.flapdoodle.mongoom.testlab.typeinfo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import de.flapdoodle.mongoom.testlab.ITypeInfo;
import de.flapdoodle.mongoom.testlab.properties.IAnnotated;

class ClassInfo implements ITypeInfo, IAnnotated {

	private final Type _type;
	private final Class<?> _declaringClass;
	private final IAnnotated _annotated;

	public ClassInfo(Class<?> declaringClass, Type clazz, IAnnotated annotated) {
		_declaringClass = declaringClass;
		_type = clazz;
		_annotated=annotated;
	}

	@Override
	public Class<?> getType() {
		if (_type instanceof Class) return (Class<?>) _type;
		return null;
	}

	@Override
	public Class<?> getDeclaringClass() {
		return _declaringClass;
	}

	@Override
	public Type getGenericType() {
		return _type;
	}
	
	@Override
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		return _annotated!=null ? _annotated.getAnnotation(annotationClass) : null;
	}
	
	@Override
	public String toString() {
		return "ClassInfo ("+_declaringClass+"(type: "+_type+")";
	}
}