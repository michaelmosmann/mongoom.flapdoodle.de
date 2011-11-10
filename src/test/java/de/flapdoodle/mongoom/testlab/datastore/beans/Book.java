package de.flapdoodle.mongoom.testlab.datastore.beans;

import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.annotations.Id;
import de.flapdoodle.mongoom.types.Reference;

@Entity("Book")
public class Book {

	@Id
	Reference<Book> _id;
	
	String _name;

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}
}
