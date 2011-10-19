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

package de.flapdoodle.mongoom.testlab.types.color;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import de.flapdoodle.mongoom.testlab.ITransformation;
import de.flapdoodle.mongoom.testlab.properties.IProperty;
import de.flapdoodle.mongoom.testlab.properties.Property;


public class ColorTransformation implements ITransformation<Color, DBObject>{

	@Override
	public DBObject asObject(Color value) {
		if (value==null) return null;
		
		BasicDBObject result = new BasicDBObject();
		result.put("r", value.getRed());
		result.put("g", value.getGreen());
		result.put("b", value.getBlue());
		result.put("a", value.getAlpha());
		return result;
	}

	@Override
	public Color asEntity(DBObject object) {
		if (object==null) return null;
		return new Color(getValue(object, "r", 0),getValue(object, "g", 0),getValue(object, "b", 0),getValue(object, "a", 0));
	}
	
	private int getValue(DBObject object, String key, int defaultValue) {
		Object value = object.get(key);
		if (value instanceof Integer) {
			return (Integer) value;
		}
		return defaultValue;
	}

	@Override
	public <Source> ITransformation<Source, ?> propertyTransformation(Property<Source> property) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<IProperty<?>> properties() {
		HashSet<IProperty<?>> result = Sets.newHashSet();
		result.add(Property.of("r",Integer.class));
		result.add(Property.of("g",Integer.class));
		result.add(Property.of("b",Integer.class));
		result.add(Property.of("a",Integer.class));
		return result;
	}

}
