package de.flapdoodle.mongoom.examples.tree;

import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.annotations.Id;
import de.flapdoodle.mongoom.types.Reference;

@Entity(value="Tree")
public class Tree
{
	@Id
	Reference<Tree> _id;
	
	Node _root;
	
	public Reference<Tree> getId()
	{
		return _id;
	}
	
	public Node getRoot()
	{
		return _root;
	}
	
	public void setRoot(Node root)
	{
		_root = root;
	}
}
