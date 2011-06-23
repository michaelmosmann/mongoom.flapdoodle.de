package de.flapdoodle.mongoom.parser.types;

import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.annotations.Id;
import de.flapdoodle.mongoom.annotations.Version;
import de.flapdoodle.mongoom.annotations.index.IndexedInGroup;
import de.flapdoodle.mongoom.annotations.index.IndexedInGroups;
import de.flapdoodle.mongoom.mapping.converter.annotations.AnnotatedClass;
import de.flapdoodle.mongoom.mapping.converter.annotations.Annotations;
import de.flapdoodle.mongoom.parser.AbstractParser;
import de.flapdoodle.mongoom.parser.IMapping;
import de.flapdoodle.mongoom.parser.IPropertyMapping;
import de.flapdoodle.mongoom.parser.IType;
import de.flapdoodle.mongoom.parser.ITypeParser;


public abstract class AbstractTypeParser extends AbstractParser implements ITypeParser {

	@Override
	public void parse(IPropertyMapping mapping, IType type) {
		Annotations.errorIfAnnotated(new AnnotatedClass(type.getType()), Entity.class, IndexedInGroup.class,
				IndexedInGroups.class);
		Annotations.errorIfAnnotated(type, Id.class, Version.class);

	}

}
