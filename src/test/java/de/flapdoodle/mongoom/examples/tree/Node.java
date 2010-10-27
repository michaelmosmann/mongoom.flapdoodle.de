package de.flapdoodle.mongoom.examples.tree;

import java.util.List;

public class Node
{
	String _name;
	
	List<Node> _childs;

	public String getName()
	{
		return _name;
	}

	public void setName(String name)
	{
		_name = name;
	}

	public List<Node> getChilds()
	{
		return _childs;
	}

	public void setChilds(List<Node> childs)
	{
		_childs = childs;
	}
	
	
}
