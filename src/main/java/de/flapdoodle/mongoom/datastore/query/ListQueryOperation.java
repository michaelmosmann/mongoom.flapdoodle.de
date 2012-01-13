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

import de.flapdoodle.mongoom.IListQueryOperation;
import de.flapdoodle.mongoom.IQuery;
import de.flapdoodle.mongoom.datastore.factories.IDBObjectFactory;
import de.flapdoodle.mongoom.exceptions.MappingException;


public class ListQueryOperation<T, Q extends IQuery<T>,V> extends AbstractQueryOperation<T, Q, V> implements IListQueryOperation<T, Q,V> {

	public ListQueryOperation(Q query, IDBObjectFactory queryBuilder, MappedNameTransformation converter) {
		super(query,queryBuilder,converter);
	}
	
	@Override
	public Q eq(V value) {
		if (_not)
			throw new MappingException("use ne instead of not.eq");
		_queryBuilder.set(_field, asObject(getConverter(true),value));
		return _query;
	}

}
