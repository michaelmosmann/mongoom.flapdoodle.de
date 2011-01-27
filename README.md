# MongoOM

MongoOM is an mongodb object mapper for java

## Why another one?

Simply because none really fitted my needs.

## Howto

### Maven

Stable

	<dependency>
	  <groupId>de.flapdoodle.mongoom</groupId>
		<artifactId>de.flapdoodle.mongoom</artifactId>
	  <version>1.1</version>
	</dependency>

Snapshots (Repository http://oss.sonatype.org/content/repositories/snapshots)

	<dependency>
	  <groupId>de.flapdoodle.mongoom</groupId>
		<artifactId>de.flapdoodle.mongoom</artifactId>
	  <version>1.2-SNAPSHOT</version>
	</dependency>

### Setup

	Mongo mongo = new Mongo( "localhost" , 27017 );
	ObjectMapper mapper = new ObjectMapper();
	mapper.map(Document.class);
	mapper.map(User.class);

	IDatastore datastore = morphia.createDatastore(mongo, "databaseName");
		
	datastore.ensureCaps();
	datastore.ensureIndexes();

### Mapping 	

#### Minimal Mapping

	@Entity("Document")
	public class Document
	{
		@Id
		ObjectId _id;
	}

#### More Complex Example

Document with multikey index, capped collection, embedded objects and readonly views
	
	@Entity(value="Document",cap=@CappedAt(count=12))
	@IndexGroups(@IndexGroup(group="multikey",options=@IndexOption(unique=true)))
	@Views(DocumentView.class)
	public class Document
	{
		// Custom Mapping between ObjectId and typed Reference
		@Id
		Reference<Document> _id;
		
		// Version support
		@Version
		String _version;
		
		// Indexed in IndexGroup
		@IndexedInGroup(group="multikey",direction=Direction.ASC)
		String _name;
		
		// Indexed
		@Indexed
		Date _created;
		
		// Custom Propertyname
		@Property(value="metainfo")
		Meta _meta;

		...
	}

Embedded Object

	public class Meta
	{
		@IndexedInGroup(group="multikey")
		List<String> _keywords;
		
		...
	}
	
View for ReadOnly Access

	public class DocumentView
	{
		String _name;

		@Property("metainfo.keywords")
		List<String> _keywords;
		
		...
	}
	
And how to use it:

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
	
	List<DocumentView> views = datastore.with(Document.class).field("metainfo.keywords").eq("One").withView(DocumentView.class).asList();
	DocumentView view = views.get(0);
	List<String> keywords = view.getKeywords();

### Query

Queries are runtime typesafe

### Callbacks

OnRead and OnWrite Callbacks


