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

package de.flapdoodle.mongoom.mapping.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;

import de.flapdoodle.mongoom.mapping.IContainerTransformation;
import de.flapdoodle.mongoom.mapping.ITransformation;
import de.flapdoodle.mongoom.mapping.properties.PropertyName;
import de.flapdoodle.mongoom.mapping.properties.TypedPropertyName;


public abstract class AbstractCollectionTransformation<Bean, Mapped, C extends Collection<Bean>> implements IContainerTransformation<Bean, Mapped, C, List<Mapped>> {

	private final Class<Bean> _collectionType;
	private final ITransformation<Bean, Mapped> _transformation;

	public AbstractCollectionTransformation(Class<Bean> collectionType, ITransformation<Bean, Mapped> transformation) {
		_collectionType = collectionType;
		_transformation = transformation;
	}

	@Override
	public List<Mapped> asObject(C value) {
		if (value == null) return null;
		ArrayList<Mapped> ret = Lists.newArrayList();
		for (Bean b : value) {
			ret.add(_transformation.asObject(b));
		}
		return ret;
	}

	@Override
	public C asEntity(List<Mapped> object) {
		if (object == null) return null;
		C ret = newContainer();
		for (Mapped v : object) {
			ret.add(_transformation.asEntity(v));
		}
		return ret;
	}

	protected abstract C newContainer();

	@Override
	public <Source> ITransformation<Source, ?> propertyTransformation(PropertyName<Source> property) {
		return _transformation.propertyTransformation(property);
	}
	
	@Override
	public <Source> PropertyName propertyName(TypedPropertyName<Source> property) {
		return _transformation.propertyName(property);
	}
//	@Override
//	public <Source> ITransformation<Source, ?> propertyTransformation(TypedPropertyName<Source> property) {
//		return _transformation.propertyTransformation(property);
//	}

	@Override
	public PropertyName propertyName(String property) {
		return _transformation.propertyName(property);
	}
	
//	@Override
//	public ITransformation<?, ?> propertyTransformation(String property) {
//		return _transformation.propertyTransformation(property);
//	}

	
	public Set<PropertyName<?>> properties() {
		return _transformation.properties();
	};
	
//	@Override
//	public Set<TypedPropertyName<?>> properties() {
//		return _transformation.properties();
//	}

	@Override
	public ITransformation<Bean, Mapped> containerConverter() {
		return _transformation;
	}

}
