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

package de.flapdoodle.mongoom;

import de.flapdoodle.mongoom.mapping.properties.PropertyReference;


public interface IEntityQuery<T> extends IQuery<T> {

	/**
	 * change to String field, Class<T> type
	 */
	@Override
	@Deprecated
	IQueryOperation<T, IEntityQuery<T>> field(String... string);

	@Override
	<V> IQueryOperation<T, IEntityQuery<T>> field(PropertyReference<V> field);
	
	@Override
	IQueryOperation<T, IEntityQuery<T>> id();

	@Override
	ISubQuery<T, IEntityQuery<T>> or();

	<V> IQueryResult<V> withView(Class<V> view);

	IQueryResult<T> result();
}
