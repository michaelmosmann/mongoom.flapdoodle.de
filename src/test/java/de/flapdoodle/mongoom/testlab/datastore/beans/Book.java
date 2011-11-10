package de.flapdoodle.mongoom.testlab.datastore.beans;

import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.annotations.Id;
import de.flapdoodle.mongoom.annotations.Version;
import de.flapdoodle.mongoom.types.Reference;

@Entity("Book")
public class Book {

	@Id
	Reference<Book> _id;
	
	@Version
	String _version;
	
	String _name;

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}
	
	@Override
	public String toString() {
		return "Book {name:"+_name+",[_id:"+_id+",_version:"+_version+"]}";
	}
}
