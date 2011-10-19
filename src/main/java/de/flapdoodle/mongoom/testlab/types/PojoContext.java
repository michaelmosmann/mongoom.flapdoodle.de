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

import java.util.Collections;
import java.util.Map;

import com.google.common.collect.Maps;

import de.flapdoodle.mongoom.testlab.IPropertyContext;
import de.flapdoodle.mongoom.testlab.ITransformation;
import de.flapdoodle.mongoom.testlab.PropertyContext;
import de.flapdoodle.mongoom.testlab.properties.Property;

class PojoContext<T> extends PropertyContext<T> {

	private final Class<T> _beanClass;

	public PojoContext(IPropertyContext<?> parentContext, Class<T> entityClass) {
		super(parentContext);
		_beanClass = entityClass;
	}

	@Override
	public <S> IPropertyContext<S> contextFor(Property<S> of) {
		return null;
	}

	public Class<T> getBeanClass() {
		return _beanClass;
	}

}
