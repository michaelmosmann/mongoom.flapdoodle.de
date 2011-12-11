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

import de.flapdoodle.mongoom.IQuery;
import de.flapdoodle.mongoom.IQueryOperation;
import de.flapdoodle.mongoom.ISubQuery;
import de.flapdoodle.mongoom.datastore.factories.IDBObjectFactory;
import de.flapdoodle.mongoom.datastore.factories.OrObjectFactory;
import de.flapdoodle.mongoom.mapping.Const;
import de.flapdoodle.mongoom.mapping.IConverter;
import de.flapdoodle.mongoom.mapping.ITypeConverter;

public class SubQuery<T, Q extends IQuery<T>> extends AbstractQuery<T, IConverter<?>> implements ISubQuery<T, Q> {

	private final Q _query;

	public SubQuery(Q query, IConverter<?> converter, IDBObjectFactory dbObjectFactory) {
		super(converter, dbObjectFactory);
		_query = query;
	}

	@Override
	public IQueryOperation<T, ISubQuery<T, Q>> field(String... field) {
		ITypeConverter<?> converter = getConverter(field);
		return new QueryOperation<T, ISubQuery<T, Q>>(this, getQueryBuilder(), field, converter);
	}

	@Override
	public IQueryOperation<T, ISubQuery<T, Q>> id() {
		return field(Const.ID_FIELDNAME);
	}

	@Override
	public ISubQuery<T, ISubQuery<T, Q>> or() {
		return new SubQuery<T, ISubQuery<T, Q>>(this, getConverter(), new OrObjectFactory(getQueryBuilder()));
	}

	@Override
	public Q parent() {
		return _query;
	}
	//	@Override
	//	public Q or(IEntityQuery<T>... queries)
	//	{
	//		super.internalOr(queries);
	//		return (Q) this;
	//	};

}
