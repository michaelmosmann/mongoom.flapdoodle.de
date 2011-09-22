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

package de.flapdoodle.mongoom.testlab;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;


public class EntityTransformation<Bean> implements ITransformation<Bean, DBObject> {

	
	private final EntityContext<Bean> _entityContext;

	public EntityTransformation(EntityContext<Bean> entityContext) {
		_entityContext = entityContext;
	}
	
	@Override
	public DBObject asObject(Bean value) {
		BasicDBObject ret = new BasicDBObject();
		Map<Property<?>, ITransformation<?, ?>> propertyTransformations = _entityContext.getPropertyTransformation();
		
		for (Property p : propertyTransformations.keySet()) {
			ITransformation transformation = propertyTransformations.get(p);
			Field field = p.getField();
			Object fieldValue=getFieldValue(field,value);
			Object dbValue = transformation.asObject(fieldValue);
			if (dbValue!=null) ret.put(p.getName(), dbValue);
		}
		return ret;
	}

	private Object getFieldValue(Field field, Bean value) {
		try {
			field.setAccessible(true);
			return field.get(value);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Bean asEntity(DBObject object) {
		return null;
	}

	@Override
	public <Source> ITransformation<Source, ?> propertyTransformation(Property<Source> property) {
		return null;
	}

	@Override
	public Set<Property<?>> properties() {
		return null;
	}

}
