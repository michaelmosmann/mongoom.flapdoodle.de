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

package de.flapdoodle.mongoom.testlab.properties;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class Property<T> implements IProperty<T> {

	private final String _name;
	private final Class<T> _type;
	private final Type _genericType;
	private final Field _field;
	private final IAnnotated _annotated;
	// MetaInfos (Index?)

	public Property(String name, Field field) {
		_name = name;
		_type = (Class<T>) field.getType();
		_genericType = field.getGenericType();
		_field=field;
		_annotated=new AnnotatedField(_field);
	}
	
	public Property(String name, Class<T> type) {
		_name=name;
		_type=type;
		_genericType=null;
		_field=null;
		_annotated=new AnnotatedClass(_type);
	}

	public String getName() {
		return _name;
	}

	public Class<T> getType() {
		return _type;
	}

	public Type getGenericType() {
		return _genericType;
	}
	
	public Field getField() {
		return _field;
	}
	
	@Override
	public IAnnotated annotated() {
		return _annotated;
	}
	
	public static Property of(String name, Field field) {
		return new Property(name, field);
	}
	
//	public static <T> Property<T> of(String name, Class<T> type) {
//		return new Property(name,type);
//	}
	
}
