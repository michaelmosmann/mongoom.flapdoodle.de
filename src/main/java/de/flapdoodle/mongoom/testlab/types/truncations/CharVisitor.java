package de.flapdoodle.mongoom.testlab.types.truncations;

import java.util.Set;

import com.google.common.collect.Sets;

import de.flapdoodle.mongoom.testlab.ITransformation;
import de.flapdoodle.mongoom.testlab.properties.TypedPropertyName;
import de.flapdoodle.mongoom.testlab.types.truncations.FloatVisitor.FloatTransformation;


public class CharVisitor extends AbstractSmallerTypeVisitor<Character,String>  {

	
	public CharVisitor() {
		super(String.class, char.class,Character.class);
	}
	
	@Override
	protected ITransformation<Character, String> newTransformation() {
		return new CharTransformation();
	}

	static class CharTransformation implements ITransformation<Character, String> {

		@Override
		public String asObject(Character value) {
			return value!=null ? value.toString() : null;
		}

		@Override
		public Character asEntity(String object) {
			return (object!=null && !object.isEmpty()) ? object.charAt(0) : null;
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
