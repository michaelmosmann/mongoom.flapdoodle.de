package de.flapdoodle.mongoom.examples.mapping;

import java.util.Date;
import java.util.List;

import com.google.inject.internal.Lists;

import de.flapdoodle.mongoom.AbstractMongoOMTest;
import de.flapdoodle.mongoom.IDatastore;
import de.flapdoodle.mongoom.ObjectMapper;

public class TestMapping extends AbstractMongoOMTest
{
	public void testDocument()
	{
		ObjectMapper mongoom = new ObjectMapper();
		mongoom.map(Document.class);

		IDatastore datastore = mongoom.createDatastore(getMongo(), getDatabaseName());
		
		datastore.ensureCaps();
		datastore.ensureIndexes();

		Document doc=new Document();
		doc.setCreated(new Date());
		doc.setName("Document One");
		Meta metaInfo = new Meta();
		metaInfo.setKeywords(Lists.newArrayList("Document","One"));
		doc.setMeta(metaInfo);
		datastore.save(doc);
		
		List<Document> list = datastore.with(Document.class).result().asList();
		assertEquals("One", 1,list.size());
		
		List<DocumentView> views = datastore.with(Document.class).field("metainfo.keywords").eq("One").withView(DocumentView.class).asList();
		DocumentView view = views.get(0);
		List<String> keywords = view.getKeywords();
		assertEquals("Keywords", true, keywords.contains("One"));
	}

	@Override
	protected boolean cleanUpAfterTest()
	{
		return false;
	}
}
