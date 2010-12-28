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

package de.flapdoodle.mongoom.datastore.factories;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.exceptions.QueryException;

public class DBObjectFactory implements IDBObjectFactory {

	Map<String, Object> _values = Maps.newLinkedHashMap();

	private DBObjectFactory() {

	}

	public static DBObjectFactory start() {
		return new DBObjectFactory();
	}

	@Override
	public DBObject get() {
		return asDBObject(_values);
	}

	private static DBObject asDBObject(Map<String, Object> values) {
		BasicDBObject ret = new BasicDBObject();
		for (String key : values.keySet()) {
			Object val = values.get(key);
			if (val instanceof DBObjectFactory) {
				DBObjectFactory cval = (DBObjectFactory) val;
				ret.put(key, cval.get());
			} else {
				if (val instanceof List) {
					List l = (List) val;
					List valList = Lists.newArrayList();
					for (Object o : l) {
						if (o instanceof DBObjectFactory) {
							valList.add(asDBObject(((DBObjectFactory) o)._values));
						} else {
							valList.add(o);
						}
					}
					ret.put(key, valList);
				} else {
					ret.put(key, val);
				}
			}
		}
		return ret;
	}

	@Override
	public DBObjectFactory set(String name, Object value) {
		Object old = _values.put(name, value);
		if (old != null)
			new QueryException(name, old, value);
		return this;
	}

	@Override
	public Object getValue(String name) {
		return _values.get(name);
	}

	@Override
	public DBObjectFactory get(String name) {
		Object old = _values.get(name);
		DBObjectFactory map = new DBObjectFactory();
		if (old != null) {
			if (old instanceof DBObjectFactory) {
				map = (DBObjectFactory) old;
				_values.put(name, map);
			} else {
				throw new MappingException("value for " + name + " allready set");
			}
		} else {
			_values.put(name, map);
		}
		return map;
	}
}
