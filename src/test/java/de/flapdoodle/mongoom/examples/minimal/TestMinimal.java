package de.flapdoodle.mongoom.examples.minimal;

import java.util.List;

import de.flapdoodle.mongoom.AbstractMongoOMTest;
import de.flapdoodle.mongoom.IDatastore;
import de.flapdoodle.mongoom.ObjectMapper;

public class TestMinimal extends AbstractMongoOMTest
{
	public void testDocument()
	{
		ObjectMapper mongoom = new ObjectMapper();
		mongoom.map(Document.class);

		IDatastore datastore = mongoom.createDatastore(getMongo(), getDatabaseName());
		
		datastore.ensureCaps();
		datastore.ensureIndexes();

		Document doc=new Document();
		datastore.save(doc);
		
		List<Document> list = datastore.with(Document.class).result().asList();
		assertEquals("One", 1,list.size());
	}
}
