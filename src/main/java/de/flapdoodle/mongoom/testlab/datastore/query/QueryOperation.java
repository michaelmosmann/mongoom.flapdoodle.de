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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;

import de.flapdoodle.mongoom.IQuery;
import de.flapdoodle.mongoom.IQueryOperation;
import de.flapdoodle.mongoom.ISubQuery;
import de.flapdoodle.mongoom.datastore.factories.IDBObjectFactory;
import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.mapping.BSONType;
import de.flapdoodle.mongoom.mapping.IContainerConverter;
import de.flapdoodle.mongoom.mapping.ITypeConverter;
import de.flapdoodle.mongoom.testlab.IContainerTransformation;
import de.flapdoodle.mongoom.testlab.ITransformation;

public class QueryOperation<T, Q extends IQuery<T>> implements IQueryOperation<T, Q> {

	private final Q _query;
	private final String _field;
	private final String[] _fields;
	private final ITransformation _converter;
	private final IDBObjectFactory _queryBuilder;

	boolean _not = false;

	public QueryOperation(Q query, IDBObjectFactory queryBuilder, String[] fields, ITransformation converter) {
		_query = query;
		_queryBuilder = queryBuilder;
		_field = asName(fields);
		_fields = fields;
		_converter = converter;
	}

	//	@Override
	public IQueryOperation<T, Q> not() {
		if (_not)
			throw new MappingException("not.not does not make any sense");
		_not = !_not;
		return this;
	}

	@Override
	public <V> Q eq(V value) {
		if (_not)
			throw new MappingException("use ne instead of not.eq");
		_queryBuilder.set(_field, getConverter(true).asObject(value));
		return _query;
	}

	@Override
	public Q match(Pattern pattern) {
		// check if dest type is String
		getConverter(false).asObject("");

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
	public <V> Q in(V... value) {
		if (_not)
			throw new MappingException("use nin instead of not.in");
		return opList("$in", true, value);
	}

	@Override
	public <V> Q all(V... value) {
		return opList("$all", true, value);
	}

	@Override
	public <V> Q nin(V... value) {
		if (_not)
			throw new MappingException("use in instead of not.nin");
		return opList("$nin", true, value);
	}

	@Override
	public <V> Q ne(V value) {
		if (_not)
			throw new MappingException("use eq instead of not.ne");
		return op("$ne", true, value);
	}

	@Override
	public <V> Q gt(V value) {
		if (_not)
			throw new MappingException("use lte instead of not.gt");
		return op("$gt", true, value);
	}

	@Override
	public <V> Q lt(V value) {
		if (_not)
			throw new MappingException("use gte instead of not.lt");
		return op("$lt", true, value);
	}

	@Override
	public <V> Q gte(V value) {
		if (_not)
			throw new MappingException("use lt instead of not.gte");
		return op("$gte", true, value);
	}

	@Override
	public <V> Q lte(V value) {
		if (_not)
			throw new MappingException("use gt instead of not.lte");
		return op("$lte", true, value);
	}

	@Override
	public SubQuery<T, Q> elemMatch() {
		if (_converter instanceof IContainerTransformation) {
			return new SubQuery<T, Q>(_query, ((IContainerTransformation) _converter).containerConverter(), _queryBuilder.get(
					_field).get("$elemMatch"));
		}
		throw new MappingException("Field " + _field + " is not an List");
	}

	private <V> Q opList(String op, boolean listAllowed, V... value) {
		List values = Lists.newArrayList();
		for (V v : value) {
			values.add(getConverter(listAllowed).asObject(v));
		}
		IDBObjectFactory factory = _queryBuilder.get(_field);
		if (_not)
			factory = factory.get("$not");
		factory.set(op, values);
		//		_queryBuilder.set(_field, op, values);
		return _query;
	};

	private <V> Q op(String op, boolean listAllowed, V value) {
		IDBObjectFactory factory = _queryBuilder.get(_field);
		if (_not)
			factory = factory.get("$not");
		factory.set(op, getConverter(listAllowed).asObject(value));
		//		_queryBuilder.set(_field, op, _converter.convertTo(value));
		return _query;
	}

	private ITransformation getConverter(boolean listAllowed) {
		if (_converter==null) throw new MappingException("No Converter for " + _field);
		if (listAllowed) {
			if (_converter instanceof IContainerConverter) {
				return ((IContainerTransformation) _converter).containerConverter();
			}
		}
		return _converter;
	}

	private static String asName(String[] field) {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String s : field) {
			if (first)
				first = false;
			else
				sb.append(".");
			sb.append(s);
		}
		return sb.toString();
	}
}
