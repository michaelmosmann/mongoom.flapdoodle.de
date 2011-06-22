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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.flapdoodle.mongoom.annotations.ConverterType;
import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.logging.LogConfig;
import de.flapdoodle.mongoom.mapping.converter.EmbeddedObjectConverter;
import de.flapdoodle.mongoom.mapping.converter.EntityConverter;
import de.flapdoodle.mongoom.mapping.converter.ITypeConverterFactory;
import de.flapdoodle.mongoom.mapping.converter.annotations.IAnnotated;
import de.flapdoodle.mongoom.mapping.converter.factories.EnumConverterFactory;
import de.flapdoodle.mongoom.mapping.converter.factories.CollectionConverterFactory;
import de.flapdoodle.mongoom.mapping.converter.factories.RawConverterFactory;
import de.flapdoodle.mongoom.mapping.converter.factories.ReferenceConverterFactory;
import de.flapdoodle.mongoom.mapping.naming.EntityAnnotationNamingFactory;
import de.flapdoodle.mongoom.mapping.naming.FieldAnnotationNamingFactory;
import de.flapdoodle.mongoom.mapping.naming.PrefixFieldNamingFactory;
import de.flapdoodle.mongoom.mapping.versions.StringVersionFactory;

public class Mapper {

	private static final Logger _logger = LogConfig.getLogger(Mapper.class);

	final List<ITypeConverterFactory<?>> _converterFactories; //= Lists.newArrayList();
//	{
//		_converterFactories.add(new RawConverterFactory());
//		_converterFactories.add(new EnumConverterFactory());
//		_converterFactories.add(new CollectionConverterFactory());
//		_converterFactories.add(new ReferenceConverterFactory());
//	}

	final List<IEntityNamingFactory> _entityNamingFactories; // = Lists.newArrayList();
//	{
//		_entityNamingFactories.add(new EntityAnnotationNamingFactory());
//	}

	final List<IFieldNamingFactory> _fieldNamingFactories; // = Lists.newArrayList();
//	{
//		_fieldNamingFactories.add(new FieldAnnotationNamingFactory());
//		_fieldNamingFactories.add(new PrefixFieldNamingFactory());
//	}

	final Map<Class<?>, IVersionFactory<?>> _versionFactories; // = Maps.newHashMap();
//	{
//		_versionFactories.put(String.class, new StringVersionFactory());
//	}

	Map<Class<?>, IEntityConverter<?>> _entityConverter = Maps.newHashMap();

	Map<Class<?>, String> _entities = Maps.newHashMap();

	
	public Mapper() {
		this(MappingConfig.getDefaults());
	}
	
	public Mapper(IMappingConfig mappingConfig) {
		_converterFactories=Lists.newArrayList(mappingConfig.getConverterFactories());
		_entityNamingFactories=Lists.newArrayList(mappingConfig.getEntityNamingFactories());
		_versionFactories=Maps.newLinkedHashMap(mappingConfig.getVersionFactories());
		_fieldNamingFactories=Lists.newArrayList(mappingConfig.getFieldNamingFactories());
	}
	
	public <T> void map(Class<T> entityClass) {
		if (_entityConverter.containsKey(entityClass))
			throw new MappingException(entityClass, "allready mapped");

		String entityName = null;
		for (IEntityNamingFactory n : _entityNamingFactories) {
			entityName = n.getEntityName(entityClass);
		}
		if (entityName == null)
			throw new MappingException(entityClass, "No EntityName, You should try at least a Entity Annotation");
		_entities.put(entityClass, entityName);
		_entityConverter.put(entityClass, new EntityConverter<T>(this, new MappingContext<T>(entityClass), entityClass));
	}

	public <M> ITypeConverter<M> map(MappingContext<?> context, Class<M> type, Type genericType,
			ConverterType converterType, IAnnotated annotations) {
		ITypeConverter<M> ret = context.getConverter(type, genericType, converterType);
		if (ret == null) {
			context.mappingStart(type, genericType, converterType);
			ret = getTypeConverter(context, type, genericType, converterType,annotations);
			context.mappingEnd(type, genericType, converterType, ret);
		}
		return ret;
	}

	private <T> ITypeConverter<T> getTypeConverter(MappingContext context, Class<?> type, Type genericType,
			ConverterType converterType, IAnnotated annotations) {
		if (converterType == null) {
			for (ITypeConverterFactory factory : _converterFactories) {
				ITypeConverter converter = factory.converter(this, context, type, genericType,annotations);
				if (converter != null)
					return converter;
			}
		}

		//		ITypeConverter ret=_converterMap.get(type);
		//		if ((ret==null) || (converterType!=null))
		ITypeConverter ret = null;
		{
			ret = getConverter(this, context, type, converterType);
		}
		return ret;
	}

	//	public void mapView(Class<?> entityClass, Class<?> view)
	//	{
	//		throw new NotImplementedException();
	//	}

	public Map<Class<?>, String> getEntities() {
		return Collections.unmodifiableMap(_entities);
	}

	public String getFieldName(Field field) {
		for (IFieldNamingFactory f : _fieldNamingFactories) {
			String name = f.getFieldName(field);
			if (name != null)
				return name;
		}
		return null;
	}

	private static ITypeConverter getConverter(Mapper mapper, MappingContext context, Class<?> m, ConverterType annotation) {
		Class<? extends ITypeConverter> converterType = EmbeddedObjectConverter.class;
		if (annotation == null)
			annotation = m.getAnnotation(ConverterType.class);
		if (annotation != null) {
			converterType = annotation.value();
		}

		Constructor<? extends ITypeConverter> constructor = null;
		try {
			constructor = converterType.getConstructor(Mapper.class, MappingContext.class, Class.class);
		} catch (SecurityException e) {
			_logger.log(Level.WARNING, "extended constructor not found", e);
		} catch (NoSuchMethodException e) {
			_logger.log(Level.WARNING, "extended constructor not found", e);
		}

		try {
			if (constructor != null) {
				return constructor.newInstance(mapper, context, m);
			} else {
				constructor = converterType.getConstructor();
				if (constructor != null) {
					return constructor.newInstance();
				} else {
					throw new MappingException(m, "No Default Constructor");
				}
			}
		} catch (InstantiationException e) {
			throw new MappingException(m, "Could not Instanciate Constructor", e);
		} catch (IllegalAccessException e) {
			throw new MappingException(m, "Could not Instanciate Constructor", e);
		} catch (InvocationTargetException e) {
			if (e.getCause() instanceof MappingException) {
				throw (MappingException) e.getCause();
			}
			throw new MappingException(m, "Could not Instanciate Constructor", e);
		} catch (SecurityException e) {
			throw new MappingException(m, "Could not Instanciate Constructor", e);
		} catch (NoSuchMethodException e) {
			throw new MappingException(m, "Could not Instanciate Constructor", e);
		}
	}

	public <T> IEntityConverter<T> getEntityConverter(Class<T> entityClass) {
		return (IEntityConverter<T>) _entityConverter.get(entityClass);
	}

	public <T> IVersionFactory<T> getVersionFactory(Class<T> versionType) {
		return (IVersionFactory<T>) _versionFactories.get(versionType);
	}

	public String getCollection(Class<? extends Object> entityClass) {
		return _entities.get(entityClass);
	}
}
