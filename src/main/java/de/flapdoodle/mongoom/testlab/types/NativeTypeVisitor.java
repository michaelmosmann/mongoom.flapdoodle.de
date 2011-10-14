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

package de.flapdoodle.mongoom.testlab.types;

import java.lang.reflect.Field;
import java.util.Set;

import com.google.common.collect.Sets;

import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.testlab.AbstractVisitor;
import de.flapdoodle.mongoom.testlab.IMappingContext;
import de.flapdoodle.mongoom.testlab.IPropertyContext;
import de.flapdoodle.mongoom.testlab.ITransformation;
import de.flapdoodle.mongoom.testlab.ITypeInfo;
import de.flapdoodle.mongoom.testlab.ITypeVisitor;
import de.flapdoodle.mongoom.testlab.Property;


public class NativeTypeVisitor<T> extends AbstractVisitor implements ITypeVisitor<T,T> {

	private final Class<T> _type;

	public NativeTypeVisitor(Class<T> type) {
		_type = type;
	}

	@Override
	public ITransformation<T, T> transformation(IMappingContext mappingContext, IPropertyContext<?> propertyContext,
			ITypeInfo field) {
		if (_type.isAssignableFrom(field.getType())) return new NoopTransformation<T>((Class<T>) field.getType());
		throw new MappingException(field.getDeclaringClass(), "Type does not match: " + _type+"!="+field.getType());
	}

	public static class NoopTransformation<N> implements ITransformation<N,N> {

		private final Class<N> _type;

		public NoopTransformation(Class<N> type) {
			_type = type;
		}

		@Override
		public N asObject(N value) {
			return value;
		}

		@Override
		public N asEntity(N object) {
			if (object==null) return null;
			
			if (!_type.isInstance(object)) throw new MappingException(_type,"could not convert "+_type+" from "+object);
			return object;
		}

		@Override
		public <Source> ITransformation<Source, ?> propertyTransformation(Property<Source> property) {
			return null;
		}

		@Override
		public Set<Property<?>> properties() {
			return null;
		}
		
	}
}