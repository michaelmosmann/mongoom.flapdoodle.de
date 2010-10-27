package de.flapdoodle.mongoom.examples.tree;

import java.util.List;

import com.google.common.collect.Lists;

import de.flapdoodle.mongoom.AbstractMongoOMTest;
import de.flapdoodle.mongoom.IDatastore;
import de.flapdoodle.mongoom.ObjectMapper;

public class TestTree extends AbstractMongoOMTest
{
	public void testTree()
	{
		ObjectMapper mongoom = new ObjectMapper();
		mongoom.map(Tree.class);

		IDatastore datastore = mongoom.createDatastore(getMongo(), getDatabaseName());
		
		datastore.ensureCaps();
		datastore.ensureIndexes();

		Tree tree=new Tree();
		tree.setRoot(buildTree(3, 0));
		
		datastore.save(tree);
		
	}
	
	private Node buildTree(int childs, int level)
	{
		Node ret=new Node();
		ret.setName("Node "+level);
		
		List<Node> childList=Lists.newArrayList();
		
		for (int i=0;i<childs;i++)
		{
			childList.add(buildTree(childs-1, level+1));
		}
		ret.setChilds(childList);
		
		return ret;
	}
	
	@Override
	protected boolean cleanUpAfterTest()
	{
		return false;
	}
}
