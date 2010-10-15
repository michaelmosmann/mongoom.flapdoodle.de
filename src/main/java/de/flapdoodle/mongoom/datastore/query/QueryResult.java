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

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import de.flapdoodle.mongoom.IQueryResult;
import de.flapdoodle.mongoom.datastore.factories.DBObjectFactory;
import de.flapdoodle.mongoom.datastore.iterator.TypedIterator;
import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.exceptions.QueryException;
import de.flapdoodle.mongoom.logging.LogConfig;
import de.flapdoodle.mongoom.mapping.IEntityConverter;
import de.flapdoodle.mongoom.mapping.IReadConverter;
import de.flapdoodle.mongoom.mapping.IViewConverter;

public class QueryResult<T> implements IQueryResult<T>
{
	private static final Logger _logger = LogConfig.getLogger(QueryResult.class);
	
	private int _limit=-1;
	private int _skip=-1;
	
	private final DBCollection _dbCollection;

	private final IReadConverter<T> _converter;

	private final DBObject _query;

	private final DBObject _view;

	private final IEntityConverter<?> _entityConverter;

	private final Map<String, Integer> _sort=Maps.newLinkedHashMap();
	
	public QueryResult(IEntityConverter<?> entityConverter, IReadConverter<T> converter, DBCollection dbCollection,DBObject query,DBObject view)
	{
		_entityConverter = entityConverter;
		_converter = converter!=null ? converter : (IReadConverter<T>) entityConverter;
			
		_dbCollection = dbCollection;
		_query = query;
		_view = view;
	}

	@Override
	public List<T> asList()
	{
		DBCursor find = getCursor();
		
		if (_limit>0) find.limit(_limit);
		if (_skip>0) find.skip(_skip);
		
		return asList(find);
	}

	@Override
	public T get()
	{
//		List<T> list = asList(_dbCollection.find(query).limit(1));
//		return list.isEmpty() ? null : list.get(0);
		DBObject one = _view!=null ? _dbCollection.findOne(_query,_view) : _dbCollection.findOne(_query);
		return one!=null ? _converter.convertFrom(one) : null;
	}
	
	@Override
	public IQueryResult<T> limit(int limit)
	{
		_limit=limit;
		return this;
	}
	
	@Override
	public IQueryResult<T> skip(int skip)
	{
		_skip=skip;
		return this;
	}
	
	@Override
	public long countAll()
	{
		return getCursor().count();
	}

	private DBCursor getCursor()
	{
		_logger.severe("Query: "+_query+", View: "+_view);
		DBCursor ret = _view!=null ? _dbCollection.find(_query,_view) : _dbCollection.find(_query);
		if (!_sort.isEmpty())
		{
			DBObjectFactory sort=DBObjectFactory.start();
			for (String name : _sort.keySet())
			{
				sort.set(name, _sort.get(name));
			}
			DBObject sortBy = sort.get();
			_logger.severe("Sort: "+sortBy);
			ret.sort(sortBy);
		}
		return ret;
	}
	
	@Override
	public Iterator<T> iterator()
	{
		DBCursor find = getCursor();
		if (_limit>0) find.limit(_limit);
		if (_skip>0) find.skip(_skip);
		return new TypedIterator<T>(this,_converter,find);
	}
	
	@Override
	public IQueryResult<T> order(String field, boolean asc)
	{
		if (_entityConverter.converter(field)==null) throw new MappingException("Field "+field+" not mapped");
		_sort.put(field, asc ? 1 : -1);
		return this;
	}
	
	private List<T> asList(DBCursor cursor)
	{
		return Lists.transform(Lists.newArrayList(cursor.iterator()),new DBObjectToEntityTransformator());
	}

	private final class DBObjectToEntityTransformator implements Function<DBObject, T>
	{
		@Override
		public T apply(DBObject dbobject)
		{
			return _converter.convertFrom(dbobject);
		}
	}
}
