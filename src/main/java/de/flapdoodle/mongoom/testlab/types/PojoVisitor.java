package de.flapdoodle.mongoom.testlab.types;

import java.util.logging.Logger;

import com.mongodb.DBObject;

import de.flapdoodle.mongoom.logging.LogConfig;
import de.flapdoodle.mongoom.testlab.AbstractClassFieldVisitor;
import de.flapdoodle.mongoom.testlab.IMappingContext;
import de.flapdoodle.mongoom.testlab.IPropertyContext;
import de.flapdoodle.mongoom.testlab.ITransformation;
import de.flapdoodle.mongoom.testlab.ITypeInfo;
import de.flapdoodle.mongoom.testlab.ITypeVisitor;


public class PojoVisitor<T> extends AbstractClassFieldVisitor<T,DBObject> implements ITypeVisitor<T, DBObject> {

	private static final Logger _logger = LogConfig.getLogger(PojoVisitor.class);
	
	@Override
	public ITransformation<T, DBObject> transformation(IMappingContext mappingContext,
			IPropertyContext<?> parentContext, ITypeInfo field) {
		_logger.severe("Starting: "+field);
		ITransformation<T, DBObject> result = (ITransformation<T, DBObject>) mappingContext.transformation(field);
		if (result==null) {
		
			PojoContext<T> rootContext=new PojoContext<T>(parentContext, (Class<T>) field.getType());
			parseProperties(mappingContext, rootContext, field);
		
			result=new PojoTransformation<T>(rootContext);
			mappingContext.setTransformation(field, result);
		}
		return result;
	}

}
