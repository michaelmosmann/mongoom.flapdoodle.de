package de.flapdoodle.mongoom.testlab.types;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.testlab.ITransformation;
import de.flapdoodle.mongoom.testlab.Property;


public class PojoTransformation<Bean>  implements ITransformation<Bean, DBObject> {

	
	private final PojoContext<Bean> _pojoContext;

	public PojoTransformation(PojoContext<Bean> context) {
		_pojoContext = context;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public DBObject asObject(Bean value) {
		BasicDBObject ret = new BasicDBObject();
		Map<Property<?>, ITransformation<?, ?>> propertyTransformations = _pojoContext.getPropertyTransformation();
		
		for (Property p : propertyTransformations.keySet()) {
			ITransformation transformation = propertyTransformations.get(p);
			Field field = p.getField();
			Object fieldValue=getFieldValue(field,value);
			Object dbValue = transformation.asObject(fieldValue);
			if (dbValue!=null) ret.put(p.getName(), dbValue);
		}
		return ret;
	}

	private Object getFieldValue(Field field, Bean value) {
		try {
			field.setAccessible(true);
			return field.get(value);
		} catch (IllegalArgumentException e) {
			throw new MappingException(_pojoContext.getBeanClass(),e);
		} catch (IllegalAccessException e) {
			throw new MappingException(_pojoContext.getBeanClass(),e);
		}
	}

	@Override
	public Bean asEntity(DBObject object) {
		Bean ret = newInstance();
		Map<Property<?>, ITransformation<?, ?>> propertyTransformations = _pojoContext.getPropertyTransformation();
		
		for (Property p : propertyTransformations.keySet()) {
			ITransformation transformation = propertyTransformations.get(p);
			Field field = p.getField();
			Object fieldValue=transformation.asEntity(object.get(p.getName()));
			if (fieldValue!=null) setFieldValue(ret, field, fieldValue);
		}
		
		return ret;
	}

	private void setFieldValue(Bean bean, Field field, Object fieldValue) {
		try {
			field.setAccessible(true);
			field.set(bean, fieldValue);
		} catch (IllegalArgumentException e) {
			throw new MappingException(_pojoContext.getBeanClass(),e);
		} catch (IllegalAccessException e) {
			throw new MappingException(_pojoContext.getBeanClass(),e);
		}
	}

	private Bean newInstance() {
		try {
			return _pojoContext.getBeanClass().newInstance();
		} catch (InstantiationException e) {
			throw new MappingException(_pojoContext.getBeanClass(),e);
		} catch (IllegalAccessException e) {
			throw new MappingException(_pojoContext.getBeanClass(),e);
		}
	}

	@Override
	public <Source> ITransformation<Source, ?> propertyTransformation(Property<Source> property) {
		return null;
	}

	@Override
	public Set<Property<?>> properties() {
		return null;
	}
	
}
