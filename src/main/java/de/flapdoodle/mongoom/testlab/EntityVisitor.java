package de.flapdoodle.mongoom.testlab;

import java.util.Map;

import com.mongodb.DBObject;

import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.annotations.Views;
import de.flapdoodle.mongoom.mapping.index.EntityIndexDef;
import de.flapdoodle.mongoom.mapping.index.IndexParser;


public class EntityVisitor<EntityBean> extends AbstractClassFieldVisitor<EntityBean,DBObject> implements IEntityVisitor<EntityBean>{

	@Override
	public ITransformation<EntityBean, DBObject> transformation(Class<EntityBean> entityClass) {
		Entity entityAnnotation = entityClass.getAnnotation(Entity.class);
		if (entityAnnotation == null) {
			error(entityClass, "Missing " + Entity.class + " Annotation");
		}
		Views viewsAnnotation = entityClass.getAnnotation(Views.class);
		
		Map<String, EntityIndexDef> indexGroupMap = IndexParser.getIndexGroupMap(entityClass);

		
		
		parseProperties(new EntityContext<EntityBean>(entityClass,entityAnnotation,viewsAnnotation,indexGroupMap),entityClass);
		
		return null;
	}


}
