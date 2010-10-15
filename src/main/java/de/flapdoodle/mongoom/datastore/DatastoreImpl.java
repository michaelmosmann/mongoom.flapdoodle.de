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

package de.flapdoodle.mongoom.datastore;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

import de.flapdoodle.collections.Lists;
import de.flapdoodle.logging.LogConfig;
import de.flapdoodle.mongoom.IDatastore;
import de.flapdoodle.mongoom.IEntityQuery;
import de.flapdoodle.mongoom.IQuery;
import de.flapdoodle.mongoom.datastore.query.Query;
import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.exceptions.ObjectMapperException;
import de.flapdoodle.mongoom.mapping.Const;
import de.flapdoodle.mongoom.mapping.IEntityConverter;
import de.flapdoodle.mongoom.mapping.Mapper;
import de.flapdoodle.mongoom.mapping.index.IndexDef;

public class DatastoreImpl implements IDatastore
{
//	static enum Operation { SAVE, INSERT, UPDATE };
	
	static final Logger _logger = LogConfig.getLogger(DatastoreImpl.class);

	private final Mapper _mapper;

	private final Mongo _mongo;

	private final String _name;

	private final DB _db;

	public DatastoreImpl(Mapper mapper, Mongo mongo, String name)
	{
		_mapper = mapper;
		_mongo = mongo;
		_name = name;

		_db = _mongo.getDB(_name);
	}
	
	@Override
	public <T> void insert(T entity)
	{
		store(Operation.Insert, entity);
	}

	@Override
	public <T> void save(T entity)
	{
		store(Operation.Save, entity);
	}
	@Override
	public <T> void update(T entity)
	{
		store(Operation.Update, entity);
	}

	private <T> void store(Operation operation, T entity)
	{
		IEntityConverter<T> converter = _mapper.getEntityConverter((Class<T>) entity.getClass());
		DBCollection dbCollection = _db.getCollection(_mapper.getCollection(entity.getClass()));
		DBObject key = converter.convertToKey(entity);
		
		if (key==null) throw new MappingException(entity.getClass(),"Key is NULL");
//		DBObject convertedEntity = converter.convertTo(entity);
		
		Object idValue = key.get(Const.ID_FIELDNAME);

		boolean reReadId=true;
		boolean mustHaveObjectId=false;
		boolean update=false;
		
		switch (operation)
		{
			case Save:
				mustHaveObjectId=true;
				break;
			case Update:
				reReadId=false;
				update=true;
				if (idValue==null) throw new MappingException(entity.getClass(),"Can not update Entities with Id not set");
				break;
		}
		
		try
		{
			_db.requestStart();
			if (mustHaveObjectId)
			{
				if ((idValue!=null) && (!(idValue instanceof ObjectId)))
				{
					throw new MappingException(entity.getClass(),"Can not save Entities with custom Id");
				}
			}
			
			converter.newVersion(entity);
			DBObject convertedEntity = converter.convertTo(entity);
			
			switch (operation)
			{
				case Insert:
					_logger.info("Insert: "+convertedEntity);
					if (idValue!=null)
					{
						_logger.log(Level.WARNING,"Insert with Id set: "+idValue,new Exception());
					}
					dbCollection.insert(convertedEntity);
					break;
				case Update:
					_logger.info("Update: "+convertedEntity+" (Id: "+idValue+")");
//					BasicDBObject updateQuery=new BasicDBObject();
//					updateQuery.put(Const.ID_FIELDNAME, idValue);
					dbCollection.update(key, convertedEntity, false, false);
					break;
				case Save:
					_logger.info("Save: "+convertedEntity);
					dbCollection.save(convertedEntity);
					break;
				default:
					throw new ObjectMapperException("Operation not supported: "+operation);
			}
			
			if (reReadId)
			{
				Object savedIdValue = convertedEntity.get(Const.ID_FIELDNAME);
				converter.setId(entity, savedIdValue);
			}
		}
		finally
		{
			Errors.checkError(_db,operation);
			_db.requestDone();
		}
		
	}
	
	public <T> void delete(T entity)
	{
		IEntityConverter<T> converter = _mapper.getEntityConverter((Class<T>) entity.getClass());
		DBCollection dbCollection = _db.getCollection(_mapper.getCollection(entity.getClass()));
		try
		{
			_db.requestStart();
			DBObject convertedEntity = converter.convertTo(entity);
			Object idValue = convertedEntity.get(Const.ID_FIELDNAME);
			if ((idValue==null) || (idValue instanceof ObjectId))
			{
				_logger.info("Delete: "+convertedEntity);
				dbCollection.remove(convertedEntity);
				converter.setId(entity, null);
			}
			else
			{
				throw new MappingException(entity.getClass(),"Can not save Entities with custom Id");
			}
		}
		finally
		{
			Errors.checkError(_db,Operation.Delete);
			_db.requestDone();
		}
	};
	
	public <T> List<T> find(Class<T> entityClass)
	{
		IEntityConverter<T> converter = _mapper.getEntityConverter(entityClass);
		DBCollection dbCollection = _db.getCollection(_mapper.getCollection(entityClass));
		DBCursor dbcursor = dbCollection.find();
		
		List<T> ret=Lists.newArrayList();
		while (dbcursor.hasNext())
		{
			ret.add(converter.convertFrom(dbcursor.next()));
		}
		return ret;
	}
	
	@Override
	public <T> IEntityQuery<T> with(Class<T> entityClass)
	{
		IEntityConverter<T> converter = _mapper.getEntityConverter(entityClass);
		DBCollection dbCollection = _db.getCollection(_mapper.getCollection(entityClass));
		return new Query<T>(converter,dbCollection);
	}

	@Override
	public void ensureCaps()
	{
		Map<Class<?>, String> entities = _mapper.getEntities();
		for (Class<?> entity : entities.keySet())
		{
			_logger.info("Ensure Caps for " + entity);
			Caps.ensureCaps(_db, entity);
		}
	}

	@Override
	public void ensureIndexes()
	{
		Map<Class<?>, String> entities = _mapper.getEntities();
		for (Class<?> entity : entities.keySet())
		{
			_logger.info("Ensure Index for " + entity);
			IEntityConverter<?> converter = _mapper.getEntityConverter(entity);
			List<IndexDef> indexes = converter.getIndexes();
			for (IndexDef def : Lists.emptyIfNull(indexes))
			{
				Indexes.ensureIndex(_db, def, entities.get(entity));
			}
		}
	}

}
