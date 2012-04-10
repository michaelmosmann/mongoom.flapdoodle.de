# MongoOM

MongoOM is an mongodb object mapper for java

## Why another one?

Simply because none really fitted my needs.

## Roadmap

- will soon use classmate for reflection stuff - https://github.com/cowtowncoder/java-classmate

## Howto

### Maven

Stable

	<dependency>
	  <groupId>de.flapdoodle.mongoom</groupId>
		<artifactId>de.flapdoodle.mongoom</artifactId>
	  <version>1.3</version>
	</dependency>

Snapshots (Repository http://oss.sonatype.org/content/repositories/snapshots)

	<dependency>
	  <groupId>de.flapdoodle.mongoom</groupId>
		<artifactId>de.flapdoodle.mongoom</artifactId>
	  <version>1.4-SNAPSHOT</version>
	</dependency>

### Setup

	Mongo mongo = new Mongo( "localhost" , 27017 );

	Set<Class<?>> classes = Sets.newLinkedHashSet();
	classes.add(Document.class);
	classes.add(User.class);

	IMappingContextFactory<?> factory = new MappingContextFactory();
	Transformations transformations = new Transformations(factory, classes);
	IDatastore datastore = new Datastore(mongo, "databaseName", transformations);
		
	datastore.ensureCaps();
	datastore.ensureIndexes();

### Mapping 	

#### Minimal Mapping

	@Entity("Document")
	public class Document
	{
		@Id
		Reference<Document> _id;
	}

#### More Complex Example

Document with multikey index, capped collection, embedded objects and readonly views
	
	@Entity(value = "Document", cap = @CappedAt(size = 1000, count = 12))
	@IndexGroups(@IndexGroup(group = "multikey", options = @IndexOption(unique = true)))
	@Views(DocumentView.class)
	public class Document {
	
		// Fieldname with Type (with removed underscore) 
		public static final PropertyReference<Meta> Meta = de.flapdoodle.mongoom.mapping.properties.Property.ref("meta",
				Meta.class);
	
		// Custom Mapping between ObjectId and typed Reference
		@Id
		Reference<Document> _id;
	
		// Version support
		@Version
		String _version;
	
		// Indexed in IndexGroup
		@IndexedInGroup(group = "multikey", direction = Direction.ASC)
		String _name;
	
		// Indexed
		@Indexed
		Date _created;
	
		// Custom Propertyname
		@Property(value = "metainfo")
		Meta _meta;

		...
	}

Embedded Object

	public class Meta
	{
		public static final PropertyReference<List<String>> Keywords = Property.ref("keywords",
				Property.listType(String.class));
	
		@IndexedInGroup(group = "multikey")
		List<String> _keywords;
		...
	}
	
View for ReadOnly Access

	public class DocumentView
	{
		String _name;
	
		@Property("metainfo.keywords")
		List<String> _keywords;
		
		public List<String> getKeywords() {
			return _keywords;
		}
	}
	
And how to use it:

	Mongo mongo = new Mongo( "localhost" , 27017 );

	Set<Class<?>> classes = Sets.newLinkedHashSet();
	classes.add(Document.class);
	
	IMappingContextFactory<?> factory = new MappingContextFactory();
	Transformations transformations = new Transformations(factory, classes);
	IDatastore datastore = new Datastore(mongo, "databaseName", transformations);
	
	datastore.ensureCaps();
	datastore.ensureIndexes();
	
	Document doc = new Document();
	doc.setCreated(new Date());
	doc.setName("Document One");
	
	Meta metaInfo = new Meta();
	ArrayList<String> keywords = Lists.newArrayList("Document", "One");
	metaInfo.setKeywords(keywords);
	
	doc.setMeta(metaInfo);
	datastore.save(doc);
	
	List<Document> list = datastore.with(Document.class).result().asList();
	
	List<DocumentView> views = datastore.with(Document.class).field(Document.Meta).listfield(Meta.
			Keywords).eq("One").withView(DocumentView.class).asList();
	DocumentView view = views.get(0);
	
	assertEquals("Equal", keywords, view.getKeywords());

### Query

Queries are runtime typesafe

### Callbacks

OnRead and OnWrite Callbacks


