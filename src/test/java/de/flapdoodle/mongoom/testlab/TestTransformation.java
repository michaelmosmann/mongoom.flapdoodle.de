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
