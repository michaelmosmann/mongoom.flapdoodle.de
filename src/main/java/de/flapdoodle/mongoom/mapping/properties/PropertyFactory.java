package de.flapdoodle.mongoom.mapping.properties;

import java.lang.reflect.Field;
import java.util.List;

import com.google.common.collect.Sets;

import de.flapdoodle.mongoom.exceptions.ObjectMapperException;
import de.flapdoodle.mongoom.mapping.converter.reflection.ClassInformation;

public class PropertyFactory {

	private PropertyFactory() {

	}

	public static <T, C> IEntityProperty<T> alias(Class<C> bean, String fieldName, Class<T> type) {
		List<Field> fields = ClassInformation.getFields(bean, Sets.newHashSet(fieldName));
		if (fields.isEmpty())
			throw new ObjectMapperException("Could not get Field " + fieldName + " on " + bean);
		Field field = fields.get(0);
		if (field.getType() != type)
			throw new ObjectMapperException("Field " + fieldName + " on " + bean + " is not of Type " + type + " but "
					+ field.getType());
		return new EntityProperty<T>(fieldName, type);
	}

	static class EntityProperty<T> implements IEntityProperty<T> {

		private final String _name;
		private final Class<T> _type;

		protected EntityProperty(String name, Class<T> type) {
			_name = name;
			_type = type;

		}

		@Override
		public String getName() {
			return _name;
		}

	}
}
