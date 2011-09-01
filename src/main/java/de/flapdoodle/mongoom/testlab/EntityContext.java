package de.flapdoodle.mongoom.testlab;

import java.util.Map;

import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.annotations.Views;
import de.flapdoodle.mongoom.mapping.index.EntityIndexDef;


public class EntityContext<EntityBean> implements IEntityContext{

	private final Class<EntityBean> _entityClass;
	private final Entity _entityAnnotation;
	private final Views _viewsAnnotation;
	private final Map<String, EntityIndexDef> _indexGroupMap;

	public EntityContext(Class<EntityBean> entityClass,Entity entityAnnotation, Views viewsAnnotation, Map<String, EntityIndexDef> indexGroupMap) {
		_entityClass = entityClass;
		_entityAnnotation = entityAnnotation;
		_viewsAnnotation = viewsAnnotation;
		_indexGroupMap = indexGroupMap;
		
	}

}
