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

package de.flapdoodle.mongoom.mapping.properties;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import de.flapdoodle.mongoom.mapping.Const;

public class Property {

	private Property() {
		throw new IllegalAccessError("singleton");
	}
	
	public static IPropertyField<?> of(PropertyName name, Field field) {
		return new PropertyWithField(name, field);
	}

	public static <T> IProperty<T> of(PropertyName name, Class<T> type) {
		return new PropertyWithClass<T>(name, type);
	}

	static class PropertyWithClass<T> implements IProperty<T> {
		
		private final PropertyName _name;
		private final Class<T> _type;
		private final IAnnotated _annotated;
		
		protected PropertyWithClass(PropertyName name, Class<T> type,IAnnotated annotated) {
			_name=name;
			_type=type;
			_annotated=annotated;
		}
		public PropertyWithClass(PropertyName name, Class<T> type) {
			this(name,type,new AnnotatedClass(type));
		}

		public PropertyName name() {
			return _name;
		}

		public Class<T> getType() {
			return _type;
		}
		
		@Override
		public IAnnotated annotated() {
			return _annotated;
		}
		

	}
	
	static class PropertyWithField<T> extends PropertyWithClass<T> implements IPropertyField<T> {
		
		private final Type _genericType;
		private final Field _field;
		// MetaInfos (Index?)

		public PropertyWithField(PropertyName name, Field field) {
			super(name,(Class<T>) field.getType(),new AnnotatedField(field));
			_genericType = field.getGenericType();
			_field=field;
		}

		@Override
		public Field getField() {
			return _field;
		}
	}
	
	public static IPropertyMappedName append(final IPropertyMappedName parent,final IPropertyMappedName current) {
		if (parent==null) return current;
		
		return new IPropertyMappedName() {
			
			@Override
			public String getMapped() {
				return parent.getMapped()+Const.FIELDNAME_SEP+current.getMapped();
			}
		};
	}
	
	public static List<String> split(String propertyName) {
		String[] path = propertyName.split("\\"+Const.FIELDNAME_SEP);
		return Arrays.asList(path);
	}
}
