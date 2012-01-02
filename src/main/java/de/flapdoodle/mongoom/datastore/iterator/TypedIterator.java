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

package de.flapdoodle.mongoom.datastore.iterator;

import java.util.Iterator;

import com.mongodb.DBCursor;

import de.flapdoodle.mongoom.datastore.query.QueryResult;
import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.mapping.IReadConverter;

public class TypedIterator<T> implements Iterator<T> {

	private final QueryResult<T> _query;
	private final DBCursor _find;
	private final IReadConverter<T> _converter;

	public TypedIterator(QueryResult<T> query, IReadConverter<T> converter, DBCursor find) {
		_query = query;
		_converter = converter;
		_find = find;
	}

	@Override
	public boolean hasNext() {
		return _find.hasNext();
	}

	@Override
	public T next() {
		return _converter.convertFrom(_find.next());
	}

	@Override
	public void remove() {
		throw new MappingException("not supported");

	}
}
