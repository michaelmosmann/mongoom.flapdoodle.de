package de.flapdoodle.mongoom.examples.mapping;

import java.util.List;

import de.flapdoodle.mongoom.annotations.index.IndexedInGroup;

public class Meta
{
	@IndexedInGroup(group="multikey")
	List<String> _keywords;
	
	public List<String> getKeywords()
	{
		return _keywords;
	}
	
	public void setKeywords(List<String> keywords)
	{
		_keywords = keywords;
	}
}
