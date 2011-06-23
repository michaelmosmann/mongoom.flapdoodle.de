package de.flapdoodle.mongoom.parser.mapping;

import java.util.Map;
import java.util.Set;

import com.google.inject.internal.Maps;
import com.google.inject.internal.Sets;

import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.parser.IEntityMapping;
import de.flapdoodle.mongoom.parser.IMapping;
import de.flapdoodle.mongoom.parser.IPropertyMapping;


public class EntityMapping implements IEntityMapping{

	private final Class<?> _entityClass;
	Map<String, FieldMapping> _properties=Maps.newLinkedHashMap();
	private String _versionProperty;
	private String _idProperty;

	public EntityMapping(Class<?> entityClass) {
		_entityClass = entityClass;
	}
	
	@Override
	public IPropertyMapping newProperty(String name) {
		if (_properties.containsKey(name)) throw new MappingException(_entityClass,"Property "+name+" allready mapped");
		FieldMapping ret = new FieldMapping(name);
		_properties.put(name, ret);
		return ret;
	}

	public void setVersionProperty(String versionProperty) {
		if (_versionProperty!=null) throw new MappingException(_entityClass,"versioned property allready set: "+_versionProperty+"->"+versionProperty);
		_versionProperty = versionProperty;
		
	}
	
	@Override
	public void setIdProperty(String idProperty) {
		if (_idProperty!=null) throw new MappingException(_entityClass,"id property allready set: "+_idProperty+"->"+idProperty);
		_idProperty = idProperty;
	}

}
