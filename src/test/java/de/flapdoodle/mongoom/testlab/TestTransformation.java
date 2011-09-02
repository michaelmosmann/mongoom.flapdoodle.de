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

import org.bson.types.ObjectId;

import com.mongodb.DBObject;

import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.types.Reference;

import junit.framework.TestCase;


public class TestTransformation extends TestCase {

	public void testReference() {
		Dummy dummy = new Dummy();
		
		ReferenceTransformation<Dummy> trans=new ReferenceTransformation<TestTransformation.Dummy>(Dummy.class);
		Reference<Dummy> reference = trans.asEntity(new ObjectId());
		assertNotNull(reference);
		ObjectId objectId = trans.asObject(reference);
		assertNotNull(objectId);
	}
	
	public void testParser() {
		EntityVisitor<Dummy> entityParser = new EntityVisitor<Dummy>();
		ITransformation<Dummy, DBObject> transformation = entityParser.transformation(Dummy.class);
		
	}
	
	@Entity("Dummy")
	static class Dummy {
		Reference<Dummy> _id;
	}
}
