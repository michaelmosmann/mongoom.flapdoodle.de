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

package de.flapdoodle.mongoom.mapping.types.color;

import java.awt.Color;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import de.flapdoodle.mongoom.mapping.ITransformation;
import de.flapdoodle.mongoom.mapping.entities.IPropertyTransformations;
import de.flapdoodle.mongoom.mapping.entities.PropertyTransformationMap;
import de.flapdoodle.mongoom.mapping.naming.PropertyName;
import de.flapdoodle.mongoom.mapping.properties.Property;
import de.flapdoodle.mongoom.mapping.properties.TypedPropertyName;
import de.flapdoodle.mongoom.mapping.types.NoopTransformation;

public class ColorTransformation implements ITransformation<Color, DBObject> {

	private IPropertyTransformations _map;

//Map<TypedPropertyName<?>, ITransformation> _propertyTransMap = Maps.newHashMap();
//Map<String, ITransformation> _propertyMap = Maps.newHashMap();

//	Map<TypedPropertyName<Integer>, ITransformation<Integer, Integer>> _propertyTransMap = Maps.newHashMap();
//	Map<String, ITransformation<Integer, Integer>> _propertyMap = Maps.newHashMap();
	{
		PropertyTransformationMap propertyMap=new PropertyTransformationMap();
		for (String name : Lists.newArrayList("r", "g", "b", "a")) {
//			_propertyTransMap.put(TypedPropertyName.of(name, Integer.class), new NoopTransformation<Integer>(Integer.class));
//			_propertyMap.put(name, new NoopTransformation<Integer>(Integer.class));
			propertyMap.setTransformation(Property.of(PropertyName.with(name, Integer.class), Integer.class), new NoopTransformation<Integer>(Integer.class));
		}
		_map=propertyMap.readOnly();
	}


	@Override
	public DBObject asObject(Color value) {
		if (value == null)
			return null;

		BasicDBObject result = new BasicDBObject();
		result.put("r", value.getRed());
		result.put("g", value.getGreen());
		result.put("b", value.getBlue());
		result.put("a", value.getAlpha());
		return result;
	}

	@Override
	public Color asEntity(DBObject object) {
		if (object == null)
			return null;
		return new Color(getValue(object, "r", 0), getValue(object, "g", 0), getValue(object, "b", 0), getValue(object,
				"a", 0));
	}

	private int getValue(DBObject object, String key, int defaultValue) {
		Object value = object.get(key);
		if (value instanceof Integer) {
			return (Integer) value;
		}
		return defaultValue;
	}

	@Override
	public Set<PropertyName<?>> properties() {
		return _map.propertyNames();
	}
	
	@Override
	public PropertyName<?> propertyName(String property) {
		return _map.get(property);
	}
	
	@Override
	public <Source> PropertyName<Source> propertyName(TypedPropertyName<Source> property) {
		return _map.get(property);
	}
	
	@Override
	public <Source> ITransformation<Source, ?> propertyTransformation(PropertyName<Source> property) {
		return _map.get(property);
	}
//	@Override
//	public <Source> ITransformation<Source, ?> propertyTransformation(TypedPropertyName<Source> property) {
//		return (ITransformation<Source, ?>) _propertyTransMap.get(property);
//	}
//	
//	@Override
//	public ITransformation<?, ?> propertyTransformation(String property) {
//		return _propertyMap.get(property);
//	}
//
//	@Override
//	public Set<TypedPropertyName<?>> properties() {
//		HashSet<TypedPropertyName<?>> result = Sets.newHashSet();
//		result.addAll(_propertyTransMap.keySet());
//		return result;
//	}

}
