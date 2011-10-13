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

package de.flapdoodle.mongoom.testlab;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;

import com.google.common.collect.Sets;
import com.mongodb.DBObject;

import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.testlab.types.ReferenceTransformation;
import de.flapdoodle.mongoom.testlab.types.SetVisitor;
import de.flapdoodle.mongoom.types.Reference;

import junit.framework.TestCase;

public class TestTransformation extends TestCase {

	public void testReference() {
		Dummy dummy = new Dummy();

		ReferenceTransformation<Dummy> trans = new ReferenceTransformation<TestTransformation.Dummy>(Dummy.class);
		Reference<Dummy> reference = trans.asEntity(new ObjectId());
		assertNotNull(reference);
		ObjectId objectId = trans.asObject(reference);
		assertNotNull(objectId);
	}

	public void testParser() {
		IMappingContext mappingContext = new MappingContext();
		EntityVisitor<Dummy> entityVisitor = new EntityVisitor<Dummy>();
		ITransformation<Dummy, DBObject> transformation = entityVisitor.transformation(mappingContext, Dummy.class);
		assertNotNull(transformation);
		Dummy dummy = newDummy();
		DBObject dbObject = transformation.asObject(dummy);
		System.out.println("DBObject:" + dbObject);
		Dummy read = transformation.asEntity(dbObject);
		System.out.println("DBObject:" + read);
		assertEquals("Eq", dummy, read);
	}

	private Dummy newDummy() {
		Dummy dummy = new Dummy();
		HashSet<String> tags = Sets.newLinkedHashSet();
		tags.add("Bla");
		tags.add("Blue");
		dummy.setTags(tags);
		Foo<Integer> foo = new Foo<Integer>();
		foo.setName("fooName");
		foo.setValue(12);
		dummy.setFoo(foo);
		dummy.setId(Reference.getInstance(Dummy.class, new ObjectId()));
		return dummy;
	}

	@Entity("Dummy")
	static class Dummy {

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

	public static class Foo<T> {

		String _name;

		T _value;

		public String getName() {
			return _name;
		}

		public void setName(String name) {
			_name = name;
		}

		public T getValue() {
			return _value;
		}

		public void setValue(T value) {
			_value = value;
		}

		@Override
		public String toString() {
			return "Foo (name: " + _name + ",value: " + _value + ")";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((_name == null)
					? 0
					: _name.hashCode());
			result = prime * result + ((_value == null)
					? 0
					: _value.hashCode());
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
			Foo other = (Foo) obj;
			if (_name == null) {
				if (other._name != null)
					return false;
			} else if (!_name.equals(other._name))
				return false;
			if (_value == null) {
				if (other._value != null)
					return false;
			} else if (!_value.equals(other._value))
				return false;
			return true;
		}

	}
}
