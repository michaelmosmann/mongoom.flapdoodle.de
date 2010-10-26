package de.flapdoodle.mongoom.mapping;

import java.util.Set;

import com.google.common.collect.Sets;

import de.flapdoodle.mongoom.exceptions.MappingException;

public class MappingContext<T>
{
	Set<Class> _inProgress=Sets.newHashSet();
	private Class<T> _entityClass;
	
	public MappingContext(Class<T> entityClass)
	{
		_entityClass=entityClass;
	}
	
	void mappingStart(Class type)
	{
		if (!_inProgress.add(type)) throw new MappingException(_entityClass, "Map again "+type+", Recursion detected");
	}
	
	void mappingEnd(Class type)
	{
		if (!_inProgress.remove(type)) throw new MappingException(_entityClass, "Map done for "+type+" but not started.");
	}

	public Class<T> getEntityClass()
	{
		return _entityClass;
	}
}
