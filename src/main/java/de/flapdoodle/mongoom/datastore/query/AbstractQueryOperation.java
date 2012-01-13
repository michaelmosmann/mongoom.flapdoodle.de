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

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

import de.flapdoodle.mongoom.IQuery;
import de.flapdoodle.mongoom.datastore.factories.IDBObjectFactory;
import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.mapping.IContainerTransformation;
import de.flapdoodle.mongoom.mapping.IQueryTransformation;
import de.flapdoodle.mongoom.mapping.ITransformation;
import de.flapdoodle.mongoom.mapping.properties.IPropertyMappedName;


public abstract class AbstractQueryOperation<T, Q extends IQuery<T>,V> {

	protected final Q _query;
	protected final String _field;
	protected final IDBObjectFactory _queryBuilder;
	protected final ITransformation _transformation;
	protected boolean _not = false;
	protected final IPropertyMappedName _name;
	
	
	public AbstractQueryOperation(Q query, IDBObjectFactory queryBuilder, MappedNameTransformation converter) {
		_query = query;
		_queryBuilder = queryBuilder;
//		_field = asName(fields);
//		_fields = fields;
		_field=converter.name().getMapped();
		_name=converter.name();
//		_converter = converter;
		_transformation=converter.transformation();
	}

	protected static <V> Object asObject(ITransformation converter, V value) {
		if (converter instanceof IQueryTransformation) {
			((IQueryTransformation) converter).asQueryObject(value);
		}
		return converter.asObject(value);
	}

	protected <V> Q opList(String op, boolean listAllowed, V... value) {
		List<V> values = Lists.newArrayList(Arrays.asList(value));
		return opList(op, listAllowed, values);
	}

	protected <V> Q opList(String op, boolean listAllowed, Collection<V> source) {
		List values=Lists.newArrayList();
		for (V v : source) {
			values.add(asObject(getConverter(listAllowed),v));
		}
		IDBObjectFactory factory = _queryBuilder.get(_field);
		if (_not)
			factory = factory.get("$not");
		factory.set(op, values);
		//		_queryBuilder.set(_field, op, values);
		return _query;
	}

	protected <V> Q op(String op, boolean listAllowed, V value) {
		IDBObjectFactory factory = _queryBuilder.get(_field);
		if (_not)
			factory = factory.get("$not");
		factory.set(op, asObject(getConverter(listAllowed),value));
		//		_queryBuilder.set(_field, op, _converter.convertTo(value));
		return _query;
	}

	protected ITransformation getConverter(boolean listAllowed) {
		if (_transformation==null) throw new MappingException("No Converter for " + _field);
		if (listAllowed) {
			if (_transformation instanceof IContainerTransformation) {
				return ((IContainerTransformation) _transformation).containerConverter();
			}
		}
		return _transformation;
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
