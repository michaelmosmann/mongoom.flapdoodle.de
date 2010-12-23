/**
 * Copyright (C) 2010 Michael Mosmann <michael@mosmann.de>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.flapdoodle.mongoom.mapping.converter;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;
import com.mongodb.DBObject;

import de.flapdoodle.mongoom.datastore.factories.DBObjectFactory;
import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.mapping.ITypeConverter;
import de.flapdoodle.mongoom.mapping.IViewConverter;
import de.flapdoodle.mongoom.mapping.Mapper;
import de.flapdoodle.mongoom.mapping.MappingContext;
import de.flapdoodle.mongoom.mapping.converter.reflection.ClassInformation;

public class ViewConverter<T> extends AbstractReadOnlyConverter<T> implements IViewConverter<T>
{
//	private final Class<T> _viewType;
//	private Constructor<T> _constructor;
	private Set<MappedAttribute> _attributes;

	public ViewConverter(Mapper mapper, EntityConverter<T> entityConverter, MappingContext<T> context,Class<T> viewType)
	{
		super(mapper,context,viewType);
		
//		_viewType = viewType;
//		_constructor = ClassAndFields.getConstructor(_viewType);
		_attributes=Sets.newLinkedHashSet();

		List<Field> fields = ClassInformation.getFields(getEntityClass());
		for (Field f : fields)
		{
			f.setAccessible(true);
			
			String fieldName=mapper.getFieldName(f);
			if (fieldName==null)
			{
				throw new MappingException(getEntityClass(),"FieldName is NULL: "+f);
			}
			
			ITypeConverter<?> converter = entityConverter.converter(fieldName);
			if (converter==null) throw new MappingException(entityConverter.getEntityClass(),"View: "+getEntityClass()+", Could not get Attribute "+fieldName+" in Entity");

			
//			converter.matchType(f.getType(),f.getGenericType());
			if (!converter.matchType(getEntityClass(),f.getType(),f.getGenericType())) throw new MappingException(entityConverter.getEntityClass(),"View: "+getEntityClass()+", converter does not match field type "+fieldName+": "+converter+" ? "+f.toGenericString()+")");
			
			MappedAttribute mappedAttribute = new MappedAttribute(f,fieldName,converter);
			if (!_attributes.add(mappedAttribute))
			{
				throw new MappingException(entityConverter.getEntityClass(),"View: "+getEntityClass()+", Field with name "+fieldName+" allready defined");
			}
			
		}
	}

//	private boolean matchType(Field source, Field dest)
//	{
//		if (dest.getType().isAssignableFrom(source.getType()))
//		{
//			// hack
//			String sourceGenType = source.getGenericType().toString();
//			String destGenType = dest.getGenericType().toString();
//			if (sourceGenType.equals(destGenType))
//			{
//				return true;
//			}
//		}
//		return false;
//	}

	@Override
	public T convertFrom(DBObject dbobject)
	{
		T ret=newInstance();
		for (MappedAttribute a : _attributes)
		{
			setEntityField(ret, a, dbobject);
		}
		return ret;
	}
	
	@Override
	public DBObject viewKeys()
	{
		DBObjectFactory factory = DBObjectFactory.start();
		for (MappedAttribute a : _attributes)
		{
			factory.set(a.getName(), 1);
		}
		return factory.get();
	}
}
