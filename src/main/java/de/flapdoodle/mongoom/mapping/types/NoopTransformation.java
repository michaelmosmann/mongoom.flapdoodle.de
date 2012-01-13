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

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;

import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.mapping.ITransformation;
import de.flapdoodle.mongoom.mapping.properties.PropertyReference;

public class NoopTransformation<N> extends AbstractPrimitiveTransformation<N,N> {

	static final Map<Class<?>,Class<?>> _objectTypeMap;
	static {
		Map<Class<?>,Class<?>> map=Maps.newHashMap();
		map.put(boolean.class,Boolean.class);
		map.put(byte.class,Byte.class);
		map.put(char.class,Character.class);
		map.put(short.class,Short.class);
		map.put(int.class,Integer.class);
		map.put(long.class,Long.class);
		map.put(float.class,Float.class);
		map.put(double.class,Double.class);
		map.put(void.class,Void.class);
		_objectTypeMap=Collections.unmodifiableMap(map);
	}
	
	private final Class<N> _type;
	private final Class<?> _objectType;
	
	public NoopTransformation(Class<N> type) {
		super(type);
		_type = type;
		if (_type.isPrimitive()) {
			_objectType=_objectTypeMap.get(type);
		} else {
			_objectType=null;
		}
	}

	@Override
	public N asObject(N value) {
		return value;
	}

	@Override
	public N asEntity(N object) {
		if (object==null) return null;
		
		if (!isInstance(object)) throw new MappingException(_type,"could not convert "+_type+" from "+object+"("+object.getClass()+")");
		return object;
	}

	private boolean isInstance(N object) {
		if (_objectType!=null) {
			return _objectType.isInstance(object);
		}
		return _type.isInstance(object);
	}
}