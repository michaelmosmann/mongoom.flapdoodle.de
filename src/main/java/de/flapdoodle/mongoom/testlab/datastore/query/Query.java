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

package de.flapdoodle.mongoom.testlab.datastore.query;

import java.util.logging.Logger;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import de.flapdoodle.mongoom.IEntityQuery;
import de.flapdoodle.mongoom.IQueryOperation;
import de.flapdoodle.mongoom.IQueryResult;
import de.flapdoodle.mongoom.ISubQuery;
import de.flapdoodle.mongoom.datastore.factories.DBObjectFactory;
import de.flapdoodle.mongoom.datastore.factories.OrObjectFactory;
import de.flapdoodle.mongoom.logging.LogConfig;
import de.flapdoodle.mongoom.mapping.Const;
import de.flapdoodle.mongoom.mapping.IEntityConverter;
import de.flapdoodle.mongoom.mapping.ITypeConverter;
import de.flapdoodle.mongoom.mapping.IViewConverter;
import de.flapdoodle.mongoom.testlab.IEntityTransformation;
import de.flapdoodle.mongoom.testlab.ITransformation;
import de.flapdoodle.mongoom.testlab.IViewTransformation;
import de.flapdoodle.mongoom.testlab.properties.TypedPropertyName;

public class Query<T> extends AbstractQuery<T, IEntityTransformation<T>> implements IEntityQuery<T> {

	private static final Logger _logger = LogConfig.getLogger(Query.class);
	private final DBCollection _dbCollection;

	public Query(IEntityTransformation<T> converter, DBCollection dbCollection) {
		super(converter, DBObjectFactory.start());
		_dbCollection = dbCollection;
	}

	@Override
	public IQueryOperation<T, IEntityQuery<T>> field(String... field) {
		ITransformation converter = getConverter(field);
		return new QueryOperation<T, IEntityQuery<T>>(this, getQueryBuilder(), field, converter);
	}

	@Override
	public IQueryOperation<T, IEntityQuery<T>> id() {
		return field(Const.ID_FIELDNAME);
	}

	@Override
	public ISubQuery<T, IEntityQuery<T>> or() {
		return new SubQuery<T, IEntityQuery<T>>(this, getConverter(), new OrObjectFactory(getQueryBuilder()));
	}

	@Override
	public <V> IQueryResult<V> withView(Class<V> view) {
		IViewTransformation<V,DBObject> viewConverter = getConverter().viewTransformation(view);
		BasicDBList viewProps=new BasicDBList();
		for (TypedPropertyName pn : viewConverter.properties()) {
			viewProps.add(pn.getName());
		}
		return new QueryResult<V>(getConverter(), viewConverter, _dbCollection, asDBObject(), viewProps);
	}

	@Override
	public IQueryResult<T> result() {
		return new QueryResult<T>(getConverter(), null, _dbCollection, asDBObject(), null);
	}
}
