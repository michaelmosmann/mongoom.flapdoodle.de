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

import org.bson.types.ObjectId;

import com.google.common.collect.Sets;
import com.mongodb.DBObject;

import de.flapdoodle.mongoom.testlab.LoopDummy.Loop;
import de.flapdoodle.mongoom.testlab.types.ReferenceTransformation;
import de.flapdoodle.mongoom.testlab.types.SetVisitor;
import de.flapdoodle.mongoom.types.Reference;

import junit.framework.TestCase;

public class TestTransformation extends TestCase {

	public void testReference() {
		Dummy dummy = new Dummy();

		ReferenceTransformation<Dummy> trans = new ReferenceTransformation<Dummy>(Dummy.class);
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
	
	public void testLoop() {
		IMappingContext mappingContext = new MappingContext();
		EntityVisitor<LoopDummy> entityVisitor = new EntityVisitor<LoopDummy>();
		ITransformation<LoopDummy, DBObject> transformation = entityVisitor.transformation(mappingContext, LoopDummy.class);
		assertNotNull(transformation);
		LoopDummy dummy = newLoopDummy();
		DBObject dbObject = transformation.asObject(dummy);
		System.out.println("DBObject:" + dbObject);
		LoopDummy read = transformation.asEntity(dbObject);
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
	
	private LoopDummy newLoopDummy() {
		LoopDummy result=new LoopDummy();
		Loop loop = new Loop();
		loop.setName("Loop");
		loop.setLoop(new Loop());
		result.setStart(loop);
		return result;
	}
}
