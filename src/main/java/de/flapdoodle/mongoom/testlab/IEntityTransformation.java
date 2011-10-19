package de.flapdoodle.mongoom.testlab;


public interface IEntityTransformation<Bean, Mapped> extends ITransformation<Bean, Mapped> {
	void newVersion(Bean value);
}
