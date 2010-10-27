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

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.flapdoodle.mongoom.annotations.ConverterType;
import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.mapping.index.IndexDef;

public class MappingContext<T>
{
	private Class<T> _entityClass;
	
	Set<Key> _inProgress=Sets.newHashSet();
	Map<Key, ITypeConverter> _converter=Maps.newHashMap();
	List<LazyTypeConverter<?>> _lazyList=Lists.newArrayList();
	
	public MappingContext(Class<T> entityClass)
	{
		_entityClass=entityClass;
	}
	
	<M> void mappingStart(Class<M> type, Type genericType, ConverterType converterType)
	{
		Key key = new Key(type,genericType,converterType);
		if (!_inProgress.add(key)) throw new MappingException(_entityClass, "Map again "+key+", Recursion detected");
	}
	
	<M> void mappingEnd(Class<M> type, Type genericType, ConverterType converterType,ITypeConverter<M> converter)
	{
		Key key = new Key(type,genericType,converterType);
		
		if (!_inProgress.remove(key)) throw new MappingException(_entityClass, "Map done for "+key+" but not started.");
		_converter.put(key, converter);
		
		cleanupLazyTypeConverter();
	}

	private void cleanupLazyTypeConverter()
	{
		if (_inProgress.isEmpty())
		{
			while (!_lazyList.isEmpty())
			{
				LazyTypeConverter<?> lazy = _lazyList.remove(0);
				ITypeConverter typeConverter = _converter.get(lazy.getKey());
				if (typeConverter==null) throw new MappingException(_entityClass,"Converter for "+lazy.getKey()+" not found");
				lazy.setTypeConverter(typeConverter);
			}
		}
	}

	public Class<T> getEntityClass()
	{
		return _entityClass;
	}

	public <M> ITypeConverter<M> getConverter(Class<M> type, Type genericType, ConverterType converterType)
	{
		Key key = new Key(type,genericType,converterType);
		if (_inProgress.contains(key))
		{
			LazyTypeConverter<M> lazyConverter = new LazyTypeConverter<M>(key);
			_lazyList.add(lazyConverter);
			return lazyConverter;
		}
		return null;
	}
	
	static class LazyTypeConverter<T> implements ITypeConverter<T>
	{
		ITypeConverter<T> _typeConverter;
		private Key _key;
		
		public LazyTypeConverter(Key key)
		{
			_key=key;
		}
		
		protected Key getKey()
		{
			return _key;
		}

		protected void setTypeConverter(ITypeConverter<T> typeConverter)
		{
			_typeConverter = typeConverter;
		}
		
		public ITypeConverter<?> converter(String field)
		{
			return _typeConverter.converter(field);
		}

		public T convertFrom(Object value)
		{
			return _typeConverter.convertFrom(value);
		}

		public Object convertTo(T value)
		{
			return _typeConverter.convertTo(value);
		}

		public List<IndexDef> getIndexes()
		{
			return _typeConverter.getIndexes();
		}

		public boolean matchType(Class<?> entityClass, Class<?> type, Type genericType)
		{
			return _typeConverter.matchType(entityClass, type, genericType);
		}
	}
	
	static class Key<M>
	{
		Class<M> _type;
		Type _genericType;
		ConverterType _converterType;
		
		public Key(Class<M> type, Type genericType, ConverterType converterType)
		{
			_type = type;
			_genericType = genericType;
			_converterType = converterType;
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + ((_converterType == null) ? 0 : _converterType.hashCode());
			result = prime * result + ((_genericType == null) ? 0 : _genericType.hashCode());
			result = prime * result + ((_type == null) ? 0 : _type.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj) return true;
			if (obj == null) return false;
			if (getClass() != obj.getClass()) return false;
			Key other = (Key) obj;
			if (_converterType == null)
			{
				if (other._converterType != null) return false;
			}
			else if (!_converterType.equals(other._converterType)) return false;
			if (_genericType == null)
			{
				if (other._genericType != null) return false;
			}
			else if (!_genericType.equals(other._genericType)) return false;
			if (_type == null)
			{
				if (other._type != null) return false;
			}
			else if (!_type.equals(other._type)) return false;
			return true;
		}

		@Override
		public String toString()
		{
			return _type+"(GenericType: "+_genericType+",ConverterType: "+_converterType+")";
		}
	}
}
