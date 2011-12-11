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

package de.flapdoodle.mongoom.testlab.mapping;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.logging.LogConfig;
import de.flapdoodle.mongoom.parser.naming.PropertyNamingListFactory;
import de.flapdoodle.mongoom.testlab.ITransformation;
import de.flapdoodle.mongoom.testlab.ITypeInfo;
import de.flapdoodle.mongoom.testlab.ITypeVisitor;
import de.flapdoodle.mongoom.testlab.properties.FieldAnnotationNaming;
import de.flapdoodle.mongoom.testlab.properties.IProperty;
import de.flapdoodle.mongoom.testlab.properties.IPropertyNaming;
import de.flapdoodle.mongoom.testlab.properties.PrefixFieldNaming;
import de.flapdoodle.mongoom.testlab.properties.Property;
import de.flapdoodle.mongoom.testlab.properties.TypedPropertyName;
import de.flapdoodle.mongoom.testlab.properties.PropertyNamingList;
import de.flapdoodle.mongoom.testlab.types.ListVisitor;
import de.flapdoodle.mongoom.testlab.types.NativeTypeVisitor;
import de.flapdoodle.mongoom.testlab.types.PojoVisitor;
import de.flapdoodle.mongoom.testlab.types.ReferenceVisitor;
import de.flapdoodle.mongoom.testlab.types.SetVisitor;
import de.flapdoodle.mongoom.testlab.versions.IVersionFactory;
import de.flapdoodle.mongoom.testlab.versions.StringVersionFactory;
import de.flapdoodle.mongoom.types.Reference;

public class MappingContext implements IMappingContext {

	private static final Logger _logger = LogConfig.getLogger(MappingContext.class);

	Map<Class<?>, ITypeVisitor> typeVisitors = Maps.newLinkedHashMap();
	{
		typeVisitors.put(Reference.class, new ReferenceVisitor());
		typeVisitors.put(Set.class, new SetVisitor());
		typeVisitors.put(List.class, new ListVisitor());
		typeVisitors.put(String.class, new NativeTypeVisitor<String>(String.class));
		typeVisitors.put(Integer.class, new NativeTypeVisitor<Integer>(Integer.class));
		typeVisitors.put(int.class, new NativeTypeVisitor<Integer>(int.class));
	}
	Map<Class<?>, IVersionFactory<?>> versionFactories = Maps.newLinkedHashMap();
	{
		versionFactories.put(String.class, new StringVersionFactory());
	}
	
	ITypeVisitor _defaultVisitor = new PojoVisitor();
	IPropertyNaming _defaultNaming = new PropertyNamingList(Lists.newArrayList(new FieldAnnotationNaming(),new PrefixFieldNaming()));
	
	@Override
	public <Type> ITypeVisitor<Type, ?> getVisitor(ITypeInfo containerType, ITypeInfo type) {
//		_logger.severe("getVisitor: " + containerType + " -> " + type);
		ITypeVisitor result = typeVisitors.get(type.getType());
		if (result == null) {
			result = _defaultVisitor;
		}
		return result;
	}

	Map<TransformationKey, ITransformation<?, ?>> _transformations = Maps.newHashMap();
	Map<TransformationKey, ProxyTransformation<?, ?>> _lazy = Maps.newHashMap();

	@Override
	public ITransformation<?, ?> transformation(ITypeInfo field) {
		TransformationKey key = TransformationKey.with(field);
		ITransformation<?, ?> result = _transformations.get(key);
		if (result == null) {
			result = _lazy.get(key);
			if (result == null) {
				ProxyTransformation proxy = new ProxyTransformation();
				_lazy.put(key, proxy);
			}
		}
		return result;
	}

	@Override
	public void setTransformation(ITypeInfo field, ITransformation<?, ?> transformation) {
		TransformationKey key = TransformationKey.with(field);
		_transformations.put(key, transformation);
		ProxyTransformation proxy = _lazy.remove(key);
		if (proxy != null) {
			proxy.setParent(transformation);
		}
	}
	
	@Override
	public IPropertyNaming naming() {
		return _defaultNaming;
	}
	
	@Override
	public IVersionFactory<?> versionFactory(ITypeInfo field) {
		return versionFactories.get(field.getType());
	}
	

	static class TransformationKey {

		private final Class<?> _type;
		private final java.lang.reflect.Type _genericType;

		public TransformationKey(Class<?> type, java.lang.reflect.Type genericType) {
			_type = type;
			_genericType = genericType;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((_genericType == null)
					? 0
					: _genericType.hashCode());
			result = prime * result + ((_type == null)
					? 0
					: _type.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TransformationKey other = (TransformationKey) obj;
			if (_genericType == null) {
				if (other._genericType != null)
					return false;
			} else if (!_genericType.equals(other._genericType))
				return false;
			if (_type == null) {
				if (other._type != null)
					return false;
			} else if (!_type.equals(other._type))
				return false;
			return true;
		}

		public static TransformationKey with(ITypeInfo typeInfo) {
			return new TransformationKey(typeInfo.getType(), typeInfo.getGenericType());
		}
	}

	static class ProxyTransformation<Bean, Mapped> implements ITransformation<Bean, Mapped> {

		ITransformation<Bean, Mapped> _parent;
		ThreadLocal<Set<Integer>> _loopBeanMap = new ThreadLocal<Set<Integer>>();

		protected void setParent(ITransformation<Bean, Mapped> parent) {
			_parent = parent;
		}

		@Override
		public Mapped asObject(Bean value) {
			boolean remove=false;
			try {
				remove=checkLoop(value);
				return _parent.asObject(value);
			} finally {
				clearLoop(remove,value);
			}
		}

		private void clearLoop(boolean remove, Bean value) {
			if (remove) {
				int hashCode = System.identityHashCode(value);
				Set<Integer> beanSet = _loopBeanMap.get();
				if (!beanSet.remove(hashCode))
				{
					_loopBeanMap.set(null);
					throw new MappingException(value.getClass(),"Something went wrong with loop detection");
				}
				if (beanSet.isEmpty()) {
					_loopBeanMap.set(null);
				}
			}
		}

		private boolean checkLoop(Bean value) {
			int hashCode = System.identityHashCode(value);
			Set<Integer> beanSet = _loopBeanMap.get();
			if (beanSet==null) {
				beanSet=Sets.newHashSet();
				_loopBeanMap.set(beanSet);
			}
			if (!beanSet.add(hashCode)) throw new MappingException(value.getClass(),"Loop detected");
			return true;
		}

		@Override
		public Bean asEntity(Mapped object) {
			return _parent.asEntity(object);
		}

		@Override
		public <Source> ITransformation<Source, ?> propertyTransformation(TypedPropertyName<Source> property) {
			return _parent.propertyTransformation(property);
		}

		@Override
		public ITransformation<?, ?> propertyTransformation(String property) {
			return _parent.propertyTransformation(property);
		}
		
		@Override
		public Set<TypedPropertyName<?>> properties() {
			return _parent.properties();
		}

	}

	public static IMappingContextFactory<MappingContext> factory() {
		return new IMappingContextFactory<MappingContext>() {
			
			@Override
			public MappingContext newContext() {
				return new MappingContext();
			}
		};
	}
}
