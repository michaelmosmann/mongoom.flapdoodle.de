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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mongodb.BasicDBList;

import de.flapdoodle.mongoom.testlab.IProperty;
import de.flapdoodle.mongoom.testlab.ITransformation;
import de.flapdoodle.mongoom.testlab.Property;
import de.flapdoodle.mongoom.types.Reference;

public class SetTransformation<Bean, Mapped> implements ITransformation<Set<Bean>, List<Mapped>> {

	private final Class<Bean> _collectionType;
	private final ITransformation<Bean, Mapped> _transformation;

	public SetTransformation(Class<Bean> collectionType, ITransformation<Bean, Mapped> transformation) {
		_collectionType = collectionType;
		_transformation = transformation;
	}

	@Override
	public List<Mapped> asObject(Set<Bean> value) {
		if (value == null) return null;
		ArrayList<Mapped> ret = Lists.newArrayList();
		for (Bean b : value) {
			ret.add(_transformation.asObject(b));
		}
		return ret;
	}

	@Override
	public Set<Bean> asEntity(List<Mapped> object) {
		if (object == null) return null;
		Set<Bean> ret = Sets.newLinkedHashSet();
		for (Mapped v : object) {
			ret.add(_transformation.asEntity(v));
		}
		return ret;
	}

	@Override
	public <Source> ITransformation<Source, ?> propertyTransformation(Property<Source> property) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<IProperty<?>> properties() {
		// TODO Auto-generated method stub
		return null;
	}

}
