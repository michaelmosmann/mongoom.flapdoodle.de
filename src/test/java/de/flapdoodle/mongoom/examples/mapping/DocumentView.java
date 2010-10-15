package de.flapdoodle.mongoom.examples.mapping;

import java.util.List;

import de.flapdoodle.mongoom.annotations.Property;

public class DocumentView
{
	String _name;

	@Property("metainfo.keywords")
	List<String> _keywords;
	
	public String getName()
	{
		return _name;
	}
	
	public List<String> getKeywords()
	{
		return _keywords;
	}
}
