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

package de.flapdoodle.mongoom.parser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;


public class FieldType implements IType {
	
	private final Field _field;

	private FieldType(Field field) {
		_field = field;
	}

	@Override
	public Class<?> getType() {
		return _field.getType();
	}

	@Override
	public Type getGenericType() {
		return _field.getGenericType();
	}
	
	@Override
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		return _field.getAnnotation(annotationClass);
	}
	
	public static FieldType of(Field field) {
		return new FieldType(field);
	}

	@Override
	public String toString() {
		if (_field.getGenericType()==_field.getType()) return "FieldType("+_field.getType()+")";
		return "FieldType("+_field.getGenericType()+")";
	}
	
}
