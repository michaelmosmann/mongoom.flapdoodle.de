package de.flapdoodle.mongoom.testlab;

import org.bson.types.ObjectId;

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
	
	static class Dummy {
		Reference<Dummy> _id;
	}
}
