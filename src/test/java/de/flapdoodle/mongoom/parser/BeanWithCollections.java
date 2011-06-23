package de.flapdoodle.mongoom.parser;

import java.util.List;
import java.util.Set;

import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.annotations.Id;
import de.flapdoodle.mongoom.annotations.index.IndexGroup;
import de.flapdoodle.mongoom.annotations.index.IndexGroups;
import de.flapdoodle.mongoom.types.Reference;


@Entity(value = "WithCollections")
public class BeanWithCollections {

	@Id
	Reference<BeanWithCollections> _id;

	Set<String> _tags;
	
	List<Album> _albums;
	
	public static class Album {
		String _name;
		int _year;
	}

}
