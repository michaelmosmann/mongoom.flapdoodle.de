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

import java.util.Collection;

import de.flapdoodle.mongoom.mapping.properties.PropertyReference;
import de.flapdoodle.mongoom.types.Reference;

public interface IQuery<T> {

//	@Deprecated
//	IQueryOperation<T, ? extends IQuery<T>> field(String... string);

	<V> IQueryOperation<T, ? extends IQuery<T>, V> field(PropertyReference<V> field);
	
	<C extends Collection<V>,V> IListQueryOperation<T, ? extends IQuery<T>, V> listfield(PropertyReference<C> field);
	
	IQueryOperation<T, ? extends IQuery<T>, Reference<T>> id();

	ISubQuery<T, ? extends IQuery<T>> or();
}
