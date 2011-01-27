package de.flapdoodle.mongoom.mapping.callbacks;


public interface IEntityReadCallback<T> {
	void onRead(T entity);
}
