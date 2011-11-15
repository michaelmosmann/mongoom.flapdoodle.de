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

package de.flapdoodle.mongoom.testlab.entities;

import de.flapdoodle.mongoom.testlab.mapping.IPropertyContext;
import de.flapdoodle.mongoom.testlab.mapping.PropertyContext;
import de.flapdoodle.mongoom.testlab.properties.IProperty;
import de.flapdoodle.mongoom.testlab.properties.Property;


public class ViewContext<ViewBean> extends AbstractBeanContext<ViewBean> implements IPropertyContext<ViewBean> {

	public ViewContext(Class<ViewBean> viewClass) {
		super(viewClass);
	}

	@Override
	public <S> IPropertyContext<S> contextFor(IProperty<S> of) {
		return new PropertyContext<S>(this);
	}

//	protected Map<PropertyName<?>, ITransformation<?, ?>> getPropertyTransformation() {
//		return Collections.unmodifiableMap(propertyTransformation);
//	}
//
//	protected Property<?> getProperty(PropertyName<?> name) {
//		return propertyMap.get(name);
//	}
}