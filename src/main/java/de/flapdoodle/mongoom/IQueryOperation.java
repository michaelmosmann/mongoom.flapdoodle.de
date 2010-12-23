/**
 * Copyright (C) 2010 Michael Mosmann <michael@mosmann.de>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.flapdoodle.mongoom;

import java.util.regex.Pattern;

public interface IQueryOperation<T, Q extends IQuery<T>> {

	Q exists(boolean exists);

	Q size(int value);

	Q mod(int mod, int eq);

	<V> Q eq(V value);

	<V> Q ne(V value);

	<V> Q gt(V value);

	<V> Q lt(V value);

	<V> Q gte(V value);

	<V> Q lte(V value);

	<V> Q all(V... value);

	<V> Q in(V... value);

	<V> Q nin(V... value);

	<V> Q type(Class<?> type);

	Q match(Pattern pattern);

	IQueryOperation<T, Q> not();

	ISubQuery<T, Q> elemMatch();
}
