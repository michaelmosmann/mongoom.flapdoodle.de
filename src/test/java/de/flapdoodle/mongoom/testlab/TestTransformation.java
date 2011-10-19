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

import java.util.HashSet;
import java.util.List;

import org.bson.types.ObjectId;

import com.google.common.collect.Sets;
import com.mongodb.DBObject;

import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.testlab.beans.BadLoopDummy;
import de.flapdoodle.mongoom.testlab.beans.Dummy;
import de.flapdoodle.mongoom.testlab.beans.Flip;
import de.flapdoodle.mongoom.testlab.beans.FlipFlopDummy;
import de.flapdoodle.mongoom.testlab.beans.Flop;
import de.flapdoodle.mongoom.testlab.beans.Foo;
import de.flapdoodle.mongoom.testlab.beans.Loop;
import de.flapdoodle.mongoom.testlab.beans.LoopDummy;
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
		IEntityTransformation<Dummy, DBObject> transformation = entityVisitor.transformation(mappingContext, Dummy.class);
		assertNotNull(transformation);
		Dummy dummy = newDummy();
		DBObject dbObject = transformation.asObject(dummy);
		System.out.println("DBObject:" + dbObject);
		Dummy read = transformation.asEntity(dbObject);
		System.out.println("DBObject:" + read);
		assertEquals("Eq", dummy, read);
		
		transformation.newVersion(read);
		assertFalse("!Eq", dummy.equals(read));
		
		dbObject = transformation.asObject(read);
		System.out.println("DBObject(new Version):" + dbObject);
		
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

	public void testFlipFlop() {
		IMappingContext mappingContext = new MappingContext();
		EntityVisitor<FlipFlopDummy> entityVisitor = new EntityVisitor<FlipFlopDummy>();
		ITransformation<FlipFlopDummy, DBObject> transformation = entityVisitor.transformation(mappingContext, FlipFlopDummy.class);
		assertNotNull(transformation);
		FlipFlopDummy dummy = newFlipFlop();
		DBObject dbObject = transformation.asObject(dummy);
		System.out.println("DBObject:" + dbObject);
		FlipFlopDummy read = transformation.asEntity(dbObject);
		System.out.println("DBObject:" + read);
		assertEquals("Eq", dummy, read);
	}
	
	public void testBadLoop() {
		IMappingContext mappingContext = new MappingContext();
		EntityVisitor<BadLoopDummy> entityVisitor = new EntityVisitor<BadLoopDummy>();
		ITransformation<BadLoopDummy, DBObject> transformation = entityVisitor.transformation(mappingContext, BadLoopDummy.class);
		assertNotNull(transformation);
		BadLoopDummy dummy = newBadLoop();
		
		MappingException ex=null;
		try {
			DBObject dbObject = transformation.asObject(dummy);
		}
		catch (MappingException mx) {
			ex=mx;
		}
		assertNotNull("Exception",ex);
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
		dummy.setMayBeVersion("12121");
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
	
	private FlipFlopDummy newFlipFlop() {
		FlipFlopDummy result=new FlipFlopDummy();
		Flip flip=new Flip();
		flip.setLevel(1);
		Flop flop=new Flop();
		flop.setLevel(2);
		Flip flip2=new Flip();
		flip2.setLevel(3);
		flop.setFlip(flip2);
		flip.setFlop(flop);
		result.setFlip(flip);
		return result;
	}
	
	private BadLoopDummy newBadLoop() {
		BadLoopDummy result = new BadLoopDummy();
		BadLoopDummy.BadLoop loop = new BadLoopDummy.BadLoop();
		BadLoopDummy.BadLoop loop2 = new BadLoopDummy.BadLoop();
		loop2.setLoop(loop);
		loop.setLoop(loop2);
		result.setLoop(loop);
		return result;
	}
	
}
