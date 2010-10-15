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

package de.flapdoodle.mongoom.mapping;

import java.util.Map;
import java.util.regex.Pattern;

import de.flapdoodle.collections.Maps;

public enum BSONType
{
	Double(1),
	String(2),
	Object(3),
	Array(4),
	Binary(5),
	ObjectId(7),
	Boolean(8),
	Date(9),
	Null(10),
	RegularExpression(11),
	JavaScript(13),
	Symbol(14),
	JavaScriptWithScope(15),
	Int32(16),
	Timestamp(17),
	Int64(18),
	MinKey(255),
	MaxKey(127);
	
	private final int _code;

	private BSONType(int code)
	{
		_code = code;
	}
	
	public int code()
	{
		return _code;
	}
	
	static Map<Class<?>,BSONType> _typeMap=Maps.newHashMap();
	static
	{
		_typeMap.put(Integer.class, Int32);
		_typeMap.put(Long.class, Int64);
		_typeMap.put(String.class, String);
		_typeMap.put(java.util.Date.class, Date);
		_typeMap.put(Double.class, Double);
		_typeMap.put(Object.class, Object);
		_typeMap.put(Object[].class, Array);
		_typeMap.put(Boolean.class, Boolean);
		_typeMap.put(org.bson.types.ObjectId.class, ObjectId);
		_typeMap.put(Pattern.class, RegularExpression);
		_typeMap.put(null, Null);
	}
	
	public static BSONType getType(Class<?> type)
	{
		return _typeMap.get(type);
	}
	
}
