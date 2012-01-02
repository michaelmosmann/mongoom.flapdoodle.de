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

package de.flapdoodle.mongoom.testlab.types.truncations;

import java.util.Arrays;
import java.util.logging.Logger;

import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.logging.LogConfig;
import de.flapdoodle.mongoom.testlab.AbstractVisitor;
import de.flapdoodle.mongoom.testlab.ITransformation;
import de.flapdoodle.mongoom.testlab.ITypeInfo;
import de.flapdoodle.mongoom.testlab.ITypeVisitor;
import de.flapdoodle.mongoom.testlab.mapping.IMappingContext;
import de.flapdoodle.mongoom.testlab.mapping.IPropertyContext;


public abstract class AbstractSmallerTypeVisitor<S,B> extends AbstractVisitor implements ITypeVisitor<S,B> {
	
	public static Logger _logger=LogConfig.getLogger(AbstractSmallerTypeVisitor.class);
	
	private final Class<?>[] _types;
	private final Class<B> _bigType;

	protected AbstractSmallerTypeVisitor(Class<B> bigType, Class<?>... types) {
		_bigType = bigType;
		_types = types;
	}
	
	@Override
	public ITransformation<S, B> transformation(IMappingContext mappingContext,
			IPropertyContext<?> propertyContext, ITypeInfo field) {
		if (isType(field)) {
			_logger.warning("Data truncation may occur for "+field+", better use this type: "+_bigType);
			return newTransformation();
		}
		throw new MappingException(field.getDeclaringClass(), "Type does not match: " + Arrays.asList(_types)+"!="+field.getType());
	}

	protected abstract ITransformation<S, B> newTransformation();

	private boolean isType(ITypeInfo field) {
		for (Class<?> type : _types) {
			if (type.isAssignableFrom(field.getType())) return true;
		}
		return false;
	}

}
