package de.flapdoodle.mongoom.testlab;

import java.util.Set;

import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.types.Reference;

@Entity("Dummy")
public class Dummy {

	Reference<Dummy> _id;

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

	@Override
	public String toString() {
		return "Dummy: " + _id + " (tags:" + _tags + ", foo: " + _foo + ")";
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
		if (_tags == null) {
			if (other._tags != null)
				return false;
		} else if (!_tags.equals(other._tags))
			return false;
		return true;
	}

}
