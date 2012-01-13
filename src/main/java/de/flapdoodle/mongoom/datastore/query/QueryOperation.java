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

import java.util.Collection;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;

import de.flapdoodle.mongoom.IListQueryOperation;
import de.flapdoodle.mongoom.IQuery;
import de.flapdoodle.mongoom.IQueryOperation;
import de.flapdoodle.mongoom.datastore.factories.IDBObjectFactory;
import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.exceptions.NotImplementedException;
import de.flapdoodle.mongoom.mapping.BSONType;
import de.flapdoodle.mongoom.mapping.IContainerTransformation;
import de.flapdoodle.mongoom.mapping.properties.PropertyReference;

public class QueryOperation<T, Q extends IQuery<T>,V> extends AbstractQueryOperation<T, Q, V> implements IQueryOperation<T, Q,V> {

	public QueryOperation(Q query, IDBObjectFactory queryBuilder, MappedNameTransformation converter) {
		super(query,queryBuilder,converter);
//		_query = query;
//		_queryBuilder = queryBuilder;
////		_field = asName(fields);
////		_fields = fields;
//		_field=converter.name().getMapped();
//		_name=converter.name();
////		_converter = converter;
//		_transformation=converter.transformation();
	}

	@Override
	public <V> IQueryOperation<T, Q,V> field(PropertyReference<V> field) {
		return new QueryOperation<T, Q,V>(_query, _queryBuilder, Queries.getConverter(field, _transformation,_name));
	}
	
	@Override
	public <C extends Collection<V>, V> IListQueryOperation<T, Q,V> listfield(PropertyReference<C> field) {
		return new ListQueryOperation<T, Q,V>(_query, _queryBuilder, Queries.getConverter(field, _transformation,_name));
	}
	
	//	@Override
	public IQueryOperation<T, Q,V> not() {
		if (_not)
			throw new MappingException("not.not does not make any sense");
		_not = !_not;
		return this;
	}

	@Override
	public Q eq(V value) {
		if (_not)
			throw new MappingException("use ne instead of not.eq");
		_queryBuilder.set(_field, asObject(getConverter(false),value));
		return _query;
	}

	@Override
	public Q match(Pattern pattern) {
		// check if dest type is String
		asObject(getConverter(false),"");

		IDBObjectFactory factory = _queryBuilder.get(_field);
		if (_not)
			factory = factory.get("$not");
		factory.set(_field, pattern);

		//		_queryBuilder.set(_field, pattern);
		return _query;
	}

	@Override
	public Q exists(boolean exists) {
		if (_not)
			throw new MappingException("use exists(" + !exists + ") instead");
		IDBObjectFactory factory = _queryBuilder.get(_field);
		//		if (_not) factory=factory.get("$not");
		factory.set("$exists", exists);
		//		_queryBuilder.set(_field, "$exists", exists);
		return _query;
	}

	@Override
	public Q size(int value) {
		IDBObjectFactory factory = _queryBuilder.get(_field);
		if (_not)
			factory = factory.get("$not");
		factory.set("$size", value);
		//		_queryBuilder.set(_field, "$size", value);
		return _query;
	}

	@Override
	public <V> Q type(Class<?> type) {
		BSONType bsonType = BSONType.getType(type);
		if (bsonType == null)
			throw new MappingException("Could not convert " + type + " to BSON Type");
		IDBObjectFactory factory = _queryBuilder.get(_field);
		if (_not)
			factory = factory.get("$not");
		factory.set("$type", bsonType.code());
		//		_queryBuilder.set(_field, "$type", bsonType);
		return _query;
	}

	@Override
	public Q mod(int mod, int eq) {
		return opList("$mod", false, Lists.newArrayList(mod, eq));
	}

	@Override
	public Q in(V... value) {
		if (_not)
			throw new MappingException("use nin instead of not.in");
		return opList("$in", true, value);
	}

	@Override
	public Q in(Collection<V> values) {
		if (_not)
			throw new MappingException("use nin instead of not.in");
		return opList("$in", true, values);
	}
	
	@Override
	public Q all(V... value) {
		return opList("$all", true, value);
	}
	
	@Override
	public Q all(Collection<V> values) {
		return opList("$all", true, values);
	}

	@Override
	public Q nin(V... value) {
		if (_not)
			throw new MappingException("use in instead of not.nin");
		return opList("$nin", true, value);
	}
	
	@Override
	public Q nin(Collection<V> values) {
		if (_not)
			throw new MappingException("use in instead of not.nin");
		return opList("$nin", true, values);
	}

	@Override
	public Q ne(V value) {
		if (_not)
			throw new MappingException("use eq instead of not.ne");
		return op("$ne", true, value);
	}

	@Override
	public Q gt(V value) {
		if (_not)
			throw new MappingException("use lte instead of not.gt");
		return op("$gt", true, value);
	}

	@Override
	public Q lt(V value) {
		if (_not)
			throw new MappingException("use gte instead of not.lt");
		return op("$lt", true, value);
	}

	@Override
	public Q gte(V value) {
		if (_not)
			throw new MappingException("use lt instead of not.gte");
		return op("$gte", true, value);
	}

	@Override
	public Q lte(V value) {
		if (_not)
			throw new MappingException("use gt instead of not.lte");
		return op("$lte", true, value);
	}

	@Override
	public SubQuery<T, Q> elemMatch() {
		if (_transformation instanceof IContainerTransformation) {
			return new SubQuery<T, Q>(_query, ((IContainerTransformation) _transformation).containerConverter(), _queryBuilder.get(
					_field).get("$elemMatch"));
		}
		throw new MappingException("Field " + _field + " is not an List");
	}
}
