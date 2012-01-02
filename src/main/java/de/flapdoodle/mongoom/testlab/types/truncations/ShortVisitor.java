package de.flapdoodle.mongoom.testlab.types.truncations;

import java.util.Set;

import com.google.common.collect.Sets;

import de.flapdoodle.mongoom.testlab.ITransformation;
import de.flapdoodle.mongoom.testlab.properties.TypedPropertyName;


public class ShortVisitor extends AbstractSmallerTypeVisitor<Short,Integer>  {

	
	public ShortVisitor() {
		super(Integer.class, short.class,Short.class);
	}
	
	@Override
	protected ITransformation<Short, Integer> newTransformation() {
		return new ShortTransformation();
	}

	static class ShortTransformation implements ITransformation<Short, Integer> {

		@Override
		public Integer asObject(Short value) {
			return value!=null ? value.intValue() : null;
		}

		@Override
		public Short asEntity(Integer object) {
			return object!=null ? object.shortValue() : null;
		}

		@Override
		public <Source> ITransformation<Source, ?> propertyTransformation(TypedPropertyName<Source> property) {
			return null;
		}

		@Override
		public ITransformation<?, ?> propertyTransformation(String property) {
			return null;
		}

		@Override
		public Set<TypedPropertyName<?>> properties() {
			return Sets.newHashSet();
		}
		
	}
}
