package de.flapdoodle.mongoom.mapping.callbacks;


public interface IEntityWriteCallback<T> {
	void onWrite(T entity);
}
