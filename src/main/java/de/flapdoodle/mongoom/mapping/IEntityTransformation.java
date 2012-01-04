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

package de.flapdoodle.mongoom.mapping;

import com.mongodb.DBObject;

import de.flapdoodle.mongoom.datastore.collections.ICollection;
import de.flapdoodle.mongoom.datastore.index.IIndex;



public interface IEntityTransformation<Bean> extends ITransformation<Bean, DBObject> {
	void newVersion(Bean value);
	Object getVersion(Bean value);
	Object getId(Bean value);
	void setId(Bean value, Object id);
	<Source> IViewTransformation<Source,DBObject> viewTransformation(Class<Source> viewType);
	
	ICollection collection();
	IIndex indexes();
}
