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

import java.util.logging.Logger;

import com.mongodb.DBCollection;

import de.flapdoodle.logging.LogConfig;
import de.flapdoodle.mongoom.IEntityQuery;
import de.flapdoodle.mongoom.IQueryOperation;
import de.flapdoodle.mongoom.IQueryResult;
import de.flapdoodle.mongoom.ISubQuery;
import de.flapdoodle.mongoom.datastore.factories.DBObjectFactory;
import de.flapdoodle.mongoom.datastore.factories.OrObjectFactory;
import de.flapdoodle.mongoom.mapping.IEntityConverter;
import de.flapdoodle.mongoom.mapping.ITypeConverter;
import de.flapdoodle.mongoom.mapping.IViewConverter;

public class Query<T> extends AbstractQuery<T,IEntityConverter<T>> implements IEntityQuery<T>
{

	private static final Logger _logger = LogConfig.getLogger(Query.class);
	private final DBCollection _dbCollection;
	
	public Query(IEntityConverter<T> converter, DBCollection dbCollection)
	{
		super(converter,DBObjectFactory.start());
		_dbCollection = dbCollection;
	}
	
	@Override
	public IQueryOperation<T,IEntityQuery<T>> field(String field)
	{
		ITypeConverter<?> converter = getConverter().converter(field);
		return new QueryOperation<T,IEntityQuery<T>>(this,getQueryBuilder(),field,converter);
	}
	
	@Override
	public ISubQuery<T,IEntityQuery<T>> or()
	{
		return new SubQuery<T,IEntityQuery<T>>(this,getConverter(),new OrObjectFactory(getQueryBuilder()));
	}

	@Override
	public <V> IQueryResult<V> withView(Class<V> view)
	{
		IViewConverter<V> viewConverter = getConverter().getView(view);
		return new QueryResult<V>(getConverter(), viewConverter, _dbCollection, asDBObject(),viewConverter.viewKeys());
	}
	
	@Override
	public IQueryResult<T> result()
	{
		return new QueryResult<T>(getConverter(), null, _dbCollection, asDBObject(),null);
	}
}
