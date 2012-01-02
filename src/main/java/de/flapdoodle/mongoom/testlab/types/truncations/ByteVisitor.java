package de.flapdoodle.mongoom.testlab.types.truncations;

import java.util.Set;

import com.google.common.collect.Sets;

import de.flapdoodle.mongoom.testlab.ITransformation;
import de.flapdoodle.mongoom.testlab.properties.TypedPropertyName;


public class ByteVisitor extends AbstractSmallerTypeVisitor<Byte,Integer>  {

	
	public ByteVisitor() {
		super(Integer.class, byte.class,Byte.class);
	}
	
	@Override
	protected ITransformation<Byte, Integer> newTransformation() {
		return new ByteTransformation();
	}

	static class ByteTransformation implements ITransformation<Byte, Integer> {

		@Override
		public Integer asObject(Byte value) {
			return value!=null ? value.intValue() : null;
		}

		@Override
		public Byte asEntity(Integer object) {
			return object!=null ? object.byteValue() : null;
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
