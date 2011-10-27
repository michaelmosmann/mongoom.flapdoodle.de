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

import com.mongodb.DBObject;

import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.testlab.IEntityTransformation;
import de.flapdoodle.mongoom.testlab.ITransformation;
import de.flapdoodle.mongoom.testlab.entities.EntityTransformation.VersionUpdater;
import de.flapdoodle.mongoom.testlab.properties.IProperty;
import de.flapdoodle.mongoom.testlab.properties.Property;
import de.flapdoodle.mongoom.testlab.versions.IVersionFactory;

public class EntityTransformation<Bean> extends AbstractBeanTransformation<Bean, EntityContext<Bean>> implements
		IEntityTransformation<Bean> {

	private VersionUpdater<Bean> _versionUpdater;

	public EntityTransformation(EntityContext<Bean> entityContext) {
		super(entityContext);
		_versionUpdater = new VersionUpdater(_entityContext.getVersionProperty(), _entityContext.getVersionFactory());
	}

	@Override
	public void newVersion(Bean value) {
		_versionUpdater.newVersion(value);
	};

	static class VersionUpdater<Bean> {

		private final Property<?> _versionProperty;
		private final IVersionFactory _versionFactory;

		public VersionUpdater(Property<?> versionProperty, IVersionFactory<?> versionFactory) {
			_versionProperty = versionProperty;
			_versionFactory = versionFactory;
		}

		public void newVersion(Bean value) {
			try {
				Object oldVersion = _versionProperty.getField().get(value);
				Object newVersion = _versionFactory.newVersion(oldVersion);
				_versionProperty.getField().set(value, newVersion);
			} catch (IllegalArgumentException e) {
				throw new MappingException(_versionProperty.getType(), e);
			} catch (IllegalAccessException e) {
				throw new MappingException(_versionProperty.getType(), e);
			}
		}
	}

	@Override
	public <Source> ITransformation<Source, DBObject> viewTransformation(Class<Source> viewType) {
		return getContext().viewTransformation(viewType);
	}

}
