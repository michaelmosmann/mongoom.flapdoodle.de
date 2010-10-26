package de.flapdoodle.mongoom.mapping;

public interface ILazyTypeConverter<T>
{
	ITypeConverter<T> getConverter();
}
