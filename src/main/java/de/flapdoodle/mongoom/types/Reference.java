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

package de.flapdoodle.mongoom.types;

import java.io.Serializable;

import org.bson.types.ObjectId;

public class Reference<T> implements Serializable, Comparable<Reference<T>> {

	public ObjectId _id;
	Class<?> _type;

	protected Reference() {

	}

	public static <T> Reference<T> getInstance(Class<T> type, ObjectId id) {
		Reference<T> ret = new Reference<T>();
		ret._id = id;
		ret._type = type;
		return ret;
	}

	public ObjectId getId() {
		return _id;
	}

	public Class<?> getType() {
		return _type;
	}

	@Override
	public String toString() {
		return "Reference(" + _type + "," + _id + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_id == null)
				? 0
				: _id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Reference other = (Reference) obj;
		if (_type != other._type)
			return false;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		return true;
	}

	@Override
	public int compareTo(Reference<T> o) {
		return _id.compareTo(o._id);
	}

}
