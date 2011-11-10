package de.flapdoodle.mongoom.testlab.datastore;

import java.util.List;

import com.google.common.collect.Lists;

import de.flapdoodle.mongoom.AbstractMongoOMTest;
import de.flapdoodle.mongoom.IDatastore;
import de.flapdoodle.mongoom.testlab.datastore.beans.Book;
import de.flapdoodle.mongoom.testlab.mapping.MappingContext;
import de.flapdoodle.mongoom.testlab.mapping.Transformations;

import junit.framework.TestCase;


public class TestDatastore extends AbstractMongoOMTest {

	public void testDatastore() {
		List<Class<?>> classes=Lists.newArrayList();
		classes.add(Book.class);
		Transformations transformations = new Transformations(MappingContext.factory(),classes);
		IDatastore datastore=new Datastore(getMongo(), getDatabaseName(), transformations);
		Book book = new Book();
		book.setName("Bla");
		datastore.save(book);
	}
	
	
}
