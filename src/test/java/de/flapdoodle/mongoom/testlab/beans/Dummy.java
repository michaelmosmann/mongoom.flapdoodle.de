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

package de.flapdoodle.mongoom.testlab.beans;

import java.util.Set;

import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.annotations.Id;
import de.flapdoodle.mongoom.annotations.Version;
import de.flapdoodle.mongoom.types.Reference;

@Entity("Dummy")
public class Dummy {

	@Id
	Reference<Dummy> _id;

	@Version
	String _mayBeVersion;

	Set<String> _tags;

	Foo<Integer> _foo;

	public Reference<Dummy> getId() {
		return _id;
	}

	public void setId(Reference<Dummy> id) {
		_id = id;
	}

	public Set<String> getTags() {
		return _tags;
	}

	public void setTags(Set<String> tags) {
		_tags = tags;
	}

	public Foo<Integer> getFoo() {
		return _foo;
	}

	public void setFoo(Foo<Integer> foo) {
		_foo = foo;
	}

	public String getMayBeVersion() {
		return _mayBeVersion;
	}

	public void setMayBeVersion(String mayBeVersion) {
		_mayBeVersion = mayBeVersion;
	}

	@Override
	public String toString() {
		return "Dummy: " + _id + "(version:" + _mayBeVersion + ") (tags:" + _tags + ", foo: " + _foo + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_foo == null)
				? 0
				: _foo.hashCode());
		result = prime * result + ((_id == null)
				? 0
				: _id.hashCode());
		result = prime * result + ((_mayBeVersion == null)
				? 0
				: _mayBeVersion.hashCode());
		result = prime * result + ((_tags == null)
				? 0
				: _tags.hashCode());
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
		Dummy other = (Dummy) obj;
		if (_foo == null) {
			if (other._foo != null)
				return false;
		} else if (!_foo.equals(other._foo))
			return false;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		if (_mayBeVersion == null) {
			if (other._mayBeVersion != null)
				return false;
		} else if (!_mayBeVersion.equals(other._mayBeVersion))
			return false;
		if (_tags == null) {
			if (other._tags != null)
				return false;
		} else if (!_tags.equals(other._tags))
			return false;
		return true;
	}

}
