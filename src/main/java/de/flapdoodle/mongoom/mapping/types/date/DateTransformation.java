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

package de.flapdoodle.mongoom.mapping.types.date;

import java.awt.Color;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import de.flapdoodle.mongoom.mapping.IQueryTransformation;
import de.flapdoodle.mongoom.mapping.ITransformation;
import de.flapdoodle.mongoom.mapping.properties.TypedPropertyName;
import de.flapdoodle.mongoom.mapping.types.NoopTransformation;


class DateTransformation implements IQueryTransformation<Date, DBObject> {

	Map<TypedPropertyName<?>, ITransformation> _propertyTransMap = Maps.newHashMap();
	Map<String, ITransformation> _propertyMap = Maps.newHashMap();
	{
		for (String name : Lists.newArrayList("y", "m", "d", "H","M","s")) {
			_propertyTransMap.put(TypedPropertyName.of(name, Integer.class), new NoopTransformation<Integer>(Integer.class));
			_propertyMap.put(name, new NoopTransformation<Integer>(Integer.class));
		}
		_propertyTransMap.put(TypedPropertyName.of("t", Date.class), new NoopTransformation<Date>(Date.class));
		_propertyMap.put("t", new NoopTransformation<Date>(Date.class));
	}


	@Override
	public DBObject asQueryObject(Date value) {
		if (value == null)
			return null;
		BasicDBObject result = new BasicDBObject();
		result.put("t", value);
		return result;
	}
	
	@Override
	public DBObject asObject(Date value) {
		if (value == null)
			return null;

		Calendar cal=Calendar.getInstance();
		cal.setTime(value);
		
		BasicDBObject result = new BasicDBObject();
		result.put("y", cal.get(Calendar.YEAR));
		result.put("m", cal.get(Calendar.MONTH));
		result.put("d", cal.get(Calendar.DAY_OF_MONTH));
		result.put("H", cal.get(Calendar.HOUR_OF_DAY));
		result.put("M", cal.get(Calendar.MINUTE));
		result.put("s", cal.get(Calendar.SECOND));
		result.put("t", value);
		return result;
	}

	@Override
	public Date asEntity(DBObject object) {
		if (object == null)
			return null;
		return (Date) object.get("t");
	}

	@Override
	public <Source> ITransformation<Source, ?> propertyTransformation(TypedPropertyName<Source> property) {
		return (ITransformation<Source, ?>) _propertyTransMap.get(property);
	}
	
	@Override
	public ITransformation<?, ?> propertyTransformation(String property) {
		return _propertyMap.get(property);
	}

	@Override
	public Set<TypedPropertyName<?>> properties() {
		HashSet<TypedPropertyName<?>> result = Sets.newHashSet();
		result.addAll(_propertyTransMap.keySet());
		return result;
	}
}
