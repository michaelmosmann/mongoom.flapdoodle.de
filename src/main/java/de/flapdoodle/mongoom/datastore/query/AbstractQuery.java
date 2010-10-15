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

package de.flapdoodle.mongoom.datastore.query;

import java.util.List;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import de.flapdoodle.collections.Lists;
import de.flapdoodle.collections.common.Function;
import de.flapdoodle.mongoom.IEntityQuery;
import de.flapdoodle.mongoom.IQuery;
import de.flapdoodle.mongoom.IQueryOperation;
import de.flapdoodle.mongoom.ISubQuery;
import de.flapdoodle.mongoom.datastore.factories.IDBObjectFactory;
import de.flapdoodle.mongoom.mapping.IConverter;
import de.flapdoodle.mongoom.mapping.IEntityConverter;
import de.flapdoodle.mongoom.mapping.ITypeConverter;

public abstract class AbstractQuery<T,C extends IConverter<?>>
{
	private final C _converter;
	private final IDBObjectFactory _queryBuilder;
	
	public AbstractQuery(C converter, IDBObjectFactory queryBuilder)
	{
		_converter = converter;
		_queryBuilder=queryBuilder;
	}
	
	protected C getConverter()
	{
		return _converter;
	}
	
	protected IDBObjectFactory getQueryBuilder()
	{
		return _queryBuilder;
	}
	
	public DBObject asDBObject()
	{
		return getQueryBuilder().get();
	}

	
	

}
