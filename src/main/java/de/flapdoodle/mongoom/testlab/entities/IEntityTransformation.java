package de.flapdoodle.mongoom.testlab.entities;

import de.flapdoodle.mongoom.testlab.ITransformation;


public interface IEntityTransformation<Bean, Mapped> extends ITransformation<Bean, Mapped> {
	void newVersion(Bean value);
}
