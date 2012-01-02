package de.flapdoodle.mongoom.testlab.types.truncations;

import java.util.Set;

import com.google.common.collect.Sets;

import de.flapdoodle.mongoom.testlab.ITransformation;
import de.flapdoodle.mongoom.testlab.properties.TypedPropertyName;


public class FloatVisitor extends AbstractSmallerTypeVisitor<Float,Double>  {

	
	public FloatVisitor() {
		super(Double.class, float.class,Float.class);
	}
	
	@Override
	protected ITransformation<Float, Double> newTransformation() {
		return new FloatTransformation();
	}

	static class FloatTransformation implements ITransformation<Float, Double> {

		@Override
		public Double asObject(Float value) {
			return value!=null ? value.doubleValue() : null;
		}

		@Override
		public Float asEntity(Double object) {
			return object!=null ? object.floatValue() : null;
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
