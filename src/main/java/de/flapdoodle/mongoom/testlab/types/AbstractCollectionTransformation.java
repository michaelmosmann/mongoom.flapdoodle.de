package de.flapdoodle.mongoom.testlab.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import de.flapdoodle.mongoom.testlab.IContainerTransformation;
import de.flapdoodle.mongoom.testlab.ITransformation;
import de.flapdoodle.mongoom.testlab.properties.TypedPropertyName;


public abstract class AbstractCollectionTransformation<Bean, Mapped, C extends Collection<Bean>> implements IContainerTransformation<Bean, Mapped, C, List<Mapped>> {

	private final Class<Bean> _collectionType;
	private final ITransformation<Bean, Mapped> _transformation;

	public AbstractCollectionTransformation(Class<Bean> collectionType, ITransformation<Bean, Mapped> transformation) {
		_collectionType = collectionType;
		_transformation = transformation;
	}

	@Override
	public List<Mapped> asObject(C value) {
		if (value == null) return null;
		ArrayList<Mapped> ret = Lists.newArrayList();
		for (Bean b : value) {
			ret.add(_transformation.asObject(b));
		}
		return ret;
	}

	@Override
	public C asEntity(List<Mapped> object) {
		if (object == null) return null;
		C ret = newContainer();
		for (Mapped v : object) {
			ret.add(_transformation.asEntity(v));
		}
		return ret;
	}

	protected abstract C newContainer();

	@Override
	public <Source> ITransformation<Source, ?> propertyTransformation(TypedPropertyName<Source> property) {
		return _transformation.propertyTransformation(property);
	}

	@Override
	public ITransformation<?, ?> propertyTransformation(String property) {
		return _transformation.propertyTransformation(property);
	}
	
	@Override
	public Set<TypedPropertyName<?>> properties() {
		return _transformation.properties();
	}

	@Override
	public ITransformation<Bean, Mapped> containerConverter() {
		return _transformation;
	}

}
