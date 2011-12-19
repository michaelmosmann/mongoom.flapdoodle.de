package de.flapdoodle.mongoom;

import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import com.google.inject.internal.Lists;

import de.flapdoodle.mongoom.logging.LogConfig;
import de.flapdoodle.mongoom.testlab.datastore.Datastore;
import de.flapdoodle.mongoom.testlab.mapping.IMappingContext;
import de.flapdoodle.mongoom.testlab.mapping.IMappingContextFactory;
import de.flapdoodle.mongoom.testlab.mapping.MappingContext;
import de.flapdoodle.mongoom.testlab.mapping.Transformations;


public abstract class AbstractDatastoreTest extends AbstractMongoOMTest {

	
	private final List<Class<?>> classes;
	private IDatastore datastore;

	public AbstractDatastoreTest(Class<?>... classes) {
		this.classes = Lists.newArrayList(classes);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		try {
		   LogManager.getLogManager().readConfiguration(getClass().getResourceAsStream("/logging.properties"));
		}catch (Exception e){
		   e.printStackTrace();
		}
		
		IMappingContextFactory<?> factory = newMappingContextFactory();
		Transformations transformations = new Transformations(factory,classes);
		IDatastore datastore=new Datastore(getMongo(), getDatabaseName(), transformations);
		datastore.ensureCaps();
		datastore.ensureIndexes();
		this.datastore=datastore;
	}

//	protected static void setLoggerLevel(Logger logger, Level level) {
//		System.out.println("Logger "+logger);
//		Handler[] handlers = logger.getHandlers();
//		for (Handler h : handlers) {
//			System.out.println("Handler "+h+" for "+logger);
//			h.setLevel(level);
//			h.setFilter(null);
//		}
//		Logger parent = logger.getParent();
//		if (parent!=null) {
//			setLoggerLevel(parent, level);
//		}
//	}
	protected IMappingContextFactory<?> newMappingContextFactory() {
		IMappingContextFactory<?> factory=new IMappingContextFactory<IMappingContext>() {
			@Override
			public IMappingContext newContext() {
				return new MappingContext();
			}
		};
		return factory;
	}
	
	public IDatastore getDatastore() {
		return datastore;
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	
}
