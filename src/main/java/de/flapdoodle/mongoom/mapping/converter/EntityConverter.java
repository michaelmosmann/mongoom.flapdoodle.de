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

package de.flapdoodle.mongoom.mapping.converter;

import java.util.Map;
import java.util.logging.Logger;

import com.google.common.collect.Maps;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.annotations.Views;
import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.logging.LogConfig;
import de.flapdoodle.mongoom.mapping.Const;
import de.flapdoodle.mongoom.mapping.IEntityConverter;
import de.flapdoodle.mongoom.mapping.IViewConverter;
import de.flapdoodle.mongoom.mapping.Mapper;

public class EntityConverter<T> extends AbstractObjectConverter<T> implements IEntityConverter<T>
{
	private static final Logger _logger = LogConfig.getLogger(EntityConverter.class);
	
	Map<Class<?>,IViewConverter<?>> _viewConverter=Maps.newHashMap();
	
	public EntityConverter(Mapper mapper,Class<T> entityClass)
	{
		super(mapper,entityClass);
		
		if (entityClass.getAnnotation(Entity.class)==null) throw new MappingException(entityClass,"Missing Entity Annotation");
		
		Views views = entityClass.getAnnotation(Views.class);
		if (views!=null)
		{
			for (Class<?> view : views.value())
			{
				_logger.severe("Map "+entityClass+" View "+view);
				if (_viewConverter.put(view, new ViewConverter(mapper,this,view))!=null)
				{
					throw new MappingException(entityClass, "View allready mapped: "+view);
				}
//				mapper.mapView(entityClass,view);
			}
		}
	}


	@Override
	protected boolean isEntity()
	{
		return true;
	}
	
	public DBObject convertTo(T entity)
	{
		return convertEntityToDBObject(entity);
	}
		
	public DBObject convertToKey(T entity)
	{
		return convertEntityToKeyDBObject(entity);
	};
	
	@Override
	public T convertFrom(DBObject dbobject)
	{
		return convertDBObjectToEntity(dbobject);
	}
	
	public void setId(T entity, Object id)
	{
		BasicDBObject dbobject = new BasicDBObject();
		dbobject.put(Const.ID_FIELDNAME, id);
		setEntityField(entity, getIdAttribute(), dbobject);
	};
	
	@Override
	public <V> IViewConverter<V> getView(Class<V> viewType)
	{
		IViewConverter<V> ret = (IViewConverter<V>) _viewConverter.get(viewType);
		if (ret==null) throw new MappingException(getEntityClass(),"View "+viewType+" not found");
		return ret;
	}


}
