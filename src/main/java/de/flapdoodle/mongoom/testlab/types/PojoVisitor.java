package de.flapdoodle.mongoom.testlab.types;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

import com.mongodb.DBObject;

import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.mapping.converter.generics.TypeExtractor;
import de.flapdoodle.mongoom.testlab.AbstractClassFieldVisitor;
import de.flapdoodle.mongoom.testlab.AbstractVisitor;
import de.flapdoodle.mongoom.testlab.IMappingContext;
import de.flapdoodle.mongoom.testlab.IPropertyContext;
import de.flapdoodle.mongoom.testlab.ITransformation;
import de.flapdoodle.mongoom.testlab.ITypeInfo;
import de.flapdoodle.mongoom.testlab.ITypeVisitor;
import de.flapdoodle.mongoom.testlab.TypeInfo;


public class PojoVisitor<T> extends AbstractClassFieldVisitor<T,DBObject> implements ITypeVisitor<T, DBObject> {

	@Override
	public ITransformation<T, DBObject> transformation(IMappingContext mappingContext,
			IPropertyContext<?> propertyContext, ITypeInfo field) {
		
		PojoContext<T> rootContext=new PojoContext<T>((Class<T>) field.getType());
		parseProperties(mappingContext, rootContext, field);
		
		return new PojoTransformation<T>(rootContext);
//		throw new MappingException(field.getDeclaringClass(), "Not implemented: "+field);
	}

}
