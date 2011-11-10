package de.flapdoodle.mongoom.testlab.datastore;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

import de.flapdoodle.mongoom.IDatastore;
import de.flapdoodle.mongoom.IEntityQuery;
import de.flapdoodle.mongoom.datastore.Errors;
import de.flapdoodle.mongoom.datastore.Operation;
import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.exceptions.ObjectMapperException;
import de.flapdoodle.mongoom.logging.LogConfig;
import de.flapdoodle.mongoom.mapping.Const;
import de.flapdoodle.mongoom.testlab.IEntityTransformation;
import de.flapdoodle.mongoom.testlab.mapping.Transformations;

public class Datastore implements IDatastore {

	private static final Logger _logger = LogConfig.getLogger(Datastore.class);
	
	
	private final Mongo _mongo;
	private final String _name;
	private final DB _db;
	private final Transformations _transformations;

	public Datastore(Mongo mongo, String name, Transformations transformations) {
		_mongo = mongo;
		_name = name;
		_transformations = transformations;
		
		_db = _mongo.getDB(_name);
	}
	
	@Override
	public void ensureCaps() {
		// TODO Auto-generated method stub

	}

	@Override
	public void ensureIndexes() {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> void insert(T entity) {
		store(Operation.Insert, entity);
	}

	@Override
	public <T> void save(T entity) {
		store(Operation.Save, entity);
	}

	@Override
	public <T> void update(T entity) {
		store(Operation.Update, entity);
	}

	private <T> void store(Operation operation, T entity) {
		IEntityTransformation<T> converter = _transformations.transformation((Class<T>) entity.getClass());
		DBCollection dbCollection = _db.getCollection(converter.getCollectionName());
		Object idValue = converter.getId(entity);
		
		
//		if (idValue == null)
//			throw new MappingException(entity.getClass(), "Key is NULL");
		//		DBObject convertedEntity = converter.convertTo(entity);

		BasicDBObject key = new BasicDBObject();
		key.put(Const.ID_FIELDNAME, idValue);
		
		boolean reReadId = true;
		boolean mustHaveObjectId = false;
		boolean update = false;

		switch (operation) {
			case Delete:
				mustHaveObjectId = true;
				reReadId = false;
				break;
			case Save:
				mustHaveObjectId = true;
				break;
			case Update:
				reReadId = false;
				update = true;
				if (idValue == null)
					throw new MappingException(entity.getClass(), "Can not update Entities with Id not set");
				break;
		}

		try {
			_db.requestStart();
			if (mustHaveObjectId) {
				if ((idValue != null) && (!(idValue instanceof ObjectId))) {
					throw new MappingException(entity.getClass(), "Can not save Entities with custom Id");
				}
			}

			converter.newVersion(entity);
			DBObject convertedEntity = converter.asObject(entity);

			switch (operation) {
				case Insert:
					_logger.fine("Insert: " + convertedEntity);
					if (idValue != null) {
						_logger.log(Level.WARNING, "Insert with Id set: " + idValue, new Exception());
					}
					dbCollection.insert(convertedEntity);
					break;
				case Update:
					_logger.fine("Update: " + convertedEntity + " (Id: " + idValue + ")");
					//					BasicDBObject updateQuery=new BasicDBObject();
					//					updateQuery.put(Const.ID_FIELDNAME, idValue);
					dbCollection.update(key, convertedEntity, false, false);
					break;
				case Save:
					_logger.fine("Save: " + convertedEntity);
					dbCollection.save(convertedEntity);
					break;
				case Delete:
					_logger.fine("Delete: " + key);
					dbCollection.remove(key);
					break;
				default:
					throw new ObjectMapperException("Operation not supported: " + operation);
			}

			if (reReadId) {
				Object savedIdValue = convertedEntity.get(Const.ID_FIELDNAME);
				converter.setId(entity, savedIdValue);
			}

			Errors.checkError(_db, operation);

			if (operation == Operation.Delete) {
				converter.setId(entity, null);
			}
		} finally {
			_db.requestDone();
		}

	}

	public <T> void delete(T entity) {
		store(Operation.Delete, entity);
	};


	@Override
	public <T> List<T> find(Class<T> entityClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> IEntityQuery<T> with(Class<T> entityClass) {
		// TODO Auto-generated method stub
		return null;
	}

}
