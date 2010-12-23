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

public interface ISubQuery<T, Q extends IQuery<T>> extends IQuery<T> {

	@Override
	IQueryOperation<T, ISubQuery<T, Q>> field(String string);

	@Override
	IQueryOperation<T, ISubQuery<T, Q>> id();

	@Override
	ISubQuery<T, ISubQuery<T, Q>> or();

	Q parent();
}
