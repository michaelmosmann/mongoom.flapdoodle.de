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

package de.flapdoodle.mongoom.testlab.entities;

import java.lang.reflect.Field;

import com.mongodb.DBObject;

import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.mapping.callbacks.IEntityReadCallback;
import de.flapdoodle.mongoom.mapping.callbacks.IEntityWriteCallback;
import de.flapdoodle.mongoom.mapping.callbacks.NoopReadCallback;
import de.flapdoodle.mongoom.mapping.callbacks.NoopWriteCallback;
import de.flapdoodle.mongoom.testlab.IEntityTransformation;
import de.flapdoodle.mongoom.testlab.ITransformation;
import de.flapdoodle.mongoom.testlab.IViewTransformation;
import de.flapdoodle.mongoom.testlab.datastore.collections.Collections;
import de.flapdoodle.mongoom.testlab.datastore.collections.ICollection;
import de.flapdoodle.mongoom.testlab.datastore.collections.ICollectionCap;
import de.flapdoodle.mongoom.testlab.datastore.index.IIndex;
import de.flapdoodle.mongoom.testlab.entities.EntityTransformation.VersionUpdater;
import de.flapdoodle.mongoom.testlab.properties.IProperty;
import de.flapdoodle.mongoom.testlab.properties.IPropertyField;
import de.flapdoodle.mongoom.testlab.properties.Property;
import de.flapdoodle.mongoom.testlab.versions.IVersionFactory;

public class EntityTransformation<Bean> extends AbstractBeanTransformation<Bean, EntityContext<Bean>> implements
		IEntityTransformation<Bean> {

	private VersionUpdater<Bean> _versionUpdater;
//	private String _collectionName;
	private ITransformation _idTransformation;
	private IPropertyField<?> _idProperty;
	private IEntityReadCallback<Bean> _readCallback;
	private IEntityWriteCallback<Bean> _writeCallback;
	private ICollection _collection;
	private IIndex _index;

	public EntityTransformation(EntityContext<Bean> entityContext) {
		super(entityContext);
//		_collectionName = entityContext.getEntityAnnotation().value();
		_versionUpdater = new VersionUpdater(_entityContext.getVersionProperty(), _entityContext.getVersionFactory());
		_idProperty = entityContext.getIdProperty();
		if (_idProperty == null)
			throw new MappingException(entityContext.getEntityClass(), "Id not set");
		_idTransformation = entityContext.getIdTransformation();
		if (_idTransformation == null)
			throw new MappingException(entityContext.getEntityClass(), "Id Transformation not set");
		
		_readCallback = entityContext.getReadCallback();
		_writeCallback = entityContext.getWriteCallback();
		if (_writeCallback==null) _writeCallback=(IEntityWriteCallback<Bean>) new NoopWriteCallback();
		if (_readCallback==null) _readCallback=(IEntityReadCallback<Bean>) new NoopReadCallback();
		
		_collection = Collections.newCollection(entityContext.getEntityAnnotation().value(), ICollectionCap.Annotated.with(getContext().getEntityAnnotation().cap()));
		_index = entityContext.index();
	}

	@Override
	public void newVersion(Bean value) {
		_versionUpdater.newVersion(value);
	};

	public Object getVersion(Bean value) {
		return _versionUpdater.getVersion(value);
	};
	
	@Override
	public Bean asEntity(DBObject object) {
		Bean ret = super.asEntity(object);
		_readCallback.onRead(ret);
		return ret;
	}
	
	public DBObject asObject(Bean value) {
		_writeCallback.onWrite(value);
		return super.asObject(value);
	};

	@Override
	public Object getId(Bean bean) {
		Field field = _idProperty.getField();
		Object fieldValue = getFieldValue(field, bean);
		Object dbValue = _idTransformation.asObject(fieldValue);
		return dbValue;
	};

	@Override
	public void setId(Bean bean, Object id) {
		Field field = _idProperty.getField();
		Object fieldValue = _idTransformation.asEntity(id);
		setFieldValue(bean, field, fieldValue);
	};

	@Override
	public ICollection collection() {
		return _collection;
	}
	
	@Override
	public IIndex indexes() {
		return _index;
	}
	
	static class VersionUpdater<Bean> {

		private final IPropertyField<?> _versionProperty;
		private final IVersionFactory _versionFactory;

		public VersionUpdater(IPropertyField<?> versionProperty, IVersionFactory<?> versionFactory) {
			_versionProperty = versionProperty;
			_versionFactory = versionFactory;
		}

		public void newVersion(Bean value) {
			if (_versionProperty != null) {
				try {
					Field field = _versionProperty.getField();
					field.setAccessible(true);
					Object oldVersion = field.get(value);
					Object newVersion = _versionFactory.newVersion(oldVersion);
					field.set(value, newVersion);
				} catch (IllegalArgumentException e) {
					throw new MappingException(_versionProperty.getType(), e);
				} catch (IllegalAccessException e) {
					throw new MappingException(_versionProperty.getType(), e);
				}
			}
		}
		
		public Object getVersion(Bean value) {
			if (_versionProperty != null) {
				try {
					Field field = _versionProperty.getField();
					field.setAccessible(true);
					Object oldVersion = field.get(value);
					return oldVersion;
				} catch (IllegalArgumentException e) {
					throw new MappingException(_versionProperty.getType(), e);
				} catch (IllegalAccessException e) {
					throw new MappingException(_versionProperty.getType(), e);
				}
			}
			return null;
		}
	}

	@Override
	public <Source> IViewTransformation<Source, DBObject> viewTransformation(Class<Source> viewType) {
		return getContext().viewTransformation(viewType);
	}

}
