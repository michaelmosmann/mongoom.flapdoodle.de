package de.flapdoodle.mongoom.testlab.types;

import java.util.List;

import com.google.common.collect.Lists;

import de.flapdoodle.mongoom.testlab.ITransformation;


public class ListTransformation<Bean, Mapped> extends AbstractCollectionTransformation<Bean, Mapped, List<Bean>> {

	public ListTransformation(Class<Bean> collectionType, ITransformation<Bean, Mapped> transformation) {
		super(collectionType,transformation);
	}

	protected List<Bean> newContainer() {
		return Lists.newArrayList();
	}
}