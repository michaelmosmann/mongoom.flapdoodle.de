package de.flapdoodle.mongoom.mapping.callbacks;


public class NoopReadCallback implements IEntityReadCallback<Object> {
	@Override
	public void onRead(Object entity) {
	}
}
