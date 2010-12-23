package de.flapdoodle.mongoom.examples.callbacks;

import java.util.List;

import de.flapdoodle.mongoom.AbstractMongoOMTest;
import de.flapdoodle.mongoom.IDatastore;
import de.flapdoodle.mongoom.ObjectMapper;

public class TestCallbacks extends AbstractMongoOMTest {

	public void testReadWrite() {
		ObjectMapper mongoom = new ObjectMapper();
		mongoom.map(Stats.class);

		IDatastore datastore = mongoom.createDatastore(getMongo(), getDatabaseName());

		datastore.ensureCaps();
		datastore.ensureIndexes();

		Stats doc = new Stats();
		doc.setA(2);
		doc.setB(3);
		datastore.save(doc);

		List<Stats> list = datastore.with(Stats.class).result().asList();
		assertEquals("One", 1, list.size());
		assertEquals("C", Integer.valueOf(5), list.get(0).getReadC());
	}

	@Override
	protected boolean cleanUpAfterTest() {
		return false;
	}
}
