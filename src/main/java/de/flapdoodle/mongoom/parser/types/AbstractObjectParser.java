package de.flapdoodle.mongoom.parser.types;

import java.lang.reflect.Field;
import java.util.List;

import de.flapdoodle.mongoom.annotations.Id;
import de.flapdoodle.mongoom.annotations.Transient;
import de.flapdoodle.mongoom.annotations.Version;
import de.flapdoodle.mongoom.annotations.index.Indexed;
import de.flapdoodle.mongoom.annotations.index.IndexedInGroup;
import de.flapdoodle.mongoom.annotations.index.IndexedInGroups;
import de.flapdoodle.mongoom.mapping.converter.annotations.Annotations;
import de.flapdoodle.mongoom.mapping.converter.reflection.ClassInformation;
import de.flapdoodle.mongoom.parser.FieldType;
import de.flapdoodle.mongoom.parser.IPropertyMapping;
import de.flapdoodle.mongoom.parser.IType;
import de.flapdoodle.mongoom.parser.ITypeParser;
import de.flapdoodle.mongoom.parser.ITypeParserFactory;


public abstract class AbstractObjectParser<T extends IPropertyMapping> extends AbstractTypeParser {

	private final ITypeParserFactory _typeParserFactory;

	
	public AbstractObjectParser(ITypeParserFactory typeParserFactory) {
		_typeParserFactory=typeParserFactory;
	}

	protected void parseFields(T mapping, IType type) {
		
		Class<?> objectClass=type.getType();
		
		
		List<Field> fields = ClassInformation.getFields(objectClass);
		
		for (Field field : fields) {
			field.setAccessible(true);
			if (field.getAnnotation(Transient.class) == null) {
				checkAnnotations(objectClass, field);

				FieldType fieldType = FieldType.of(field);
				
				ITypeParser parser = _typeParserFactory.getParser(fieldType);
				if (parser==null) error(type,"no parser for "+field);
				
				IPropertyMapping property = mapping.newProperty(field.getName());
				parser.parse(property, fieldType);
				
				postProcessProperty(mapping,fieldType,field.getName());
			}
		}

	}

	protected void postProcessProperty(T mapping, FieldType fieldType,String name) {
		
	}

	protected void checkAnnotations(Class<?> objectClass, Field field) {
		Annotations.errorIfAnnotated(objectClass, field, Id.class, Version.class);
		Annotations.checkForOnlyOneAnnotation(objectClass, field, Indexed.class, IndexedInGroup.class,
				IndexedInGroups.class);
	}

}
