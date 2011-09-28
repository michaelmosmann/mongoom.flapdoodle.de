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

package de.flapdoodle.mongoom.testlab;

import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;

import com.google.common.collect.Sets;
import com.mongodb.DBObject;

import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.testlab.types.ReferenceTransformation;
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
		EntityVisitor<Dummy> entityParser = new EntityVisitor<Dummy>();
		ITransformation<Dummy, DBObject> transformation = entityParser.transformation(mappingContext, Dummy.class);
		assertNotNull(transformation);
		Dummy dummy = new Dummy();
		dummy.setTags(Sets.newHashSet("Bla","Blu"));
		dummy.setId(Reference.getInstance(Dummy.class, new ObjectId()));
		DBObject dbObject = transformation.asObject(dummy);
		System.out.println("DBObject:" + dbObject);
		Dummy read = transformation.asEntity(dbObject);
		System.out.println("DBObject:" + read);
		assertEquals("Eq", dummy, read);
	}

	@Entity("Dummy")
	static class Dummy {

		Reference<Dummy> _id;

		Set<String> _tags;

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

		@Override
		public String toString() {
			return "Dummy: " + _id + " (tags:" + _tags + ")";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
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
}
