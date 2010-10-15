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

package de.flapdoodle.mongoom.mapping.converter.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import com.google.common.collect.Lists;

import de.flapdoodle.mongoom.annotations.MappedSuperclass;
import de.flapdoodle.mongoom.exceptions.MappingException;

public class ClassAndFields
{
	private ClassAndFields()
	{
		
	}
	
	public static <T> Constructor<T> getConstructor(Class<T> entityClass)
	{
		try
		{
			for (Constructor c : entityClass.getDeclaredConstructors())
			{
				if (c.getParameterTypes().length==0)
				{
					c.setAccessible(true);
					return c;
				}
			}
		}
		catch (SecurityException e)
		{
			throw new MappingException(entityClass,"getConstructor()",e);
		}
		throw new MappingException(entityClass,"No default Constuctor");
	}

	public static <T> List<Field> getFields(Class<? super T> entityClass)
	{
		List<Field> ret=Lists.newArrayList();
		
		Class<? super T> superclass = entityClass.getSuperclass();
		if (superclass.getAnnotation(MappedSuperclass.class)!=null)
		{
			ret.addAll(getFields(superclass));
		}
		
		Field[] declaredFields = entityClass.getDeclaredFields();
		for (Field f : declaredFields)
		{
			int modifier = f.getModifiers();
			if (!Modifier.isStatic(modifier))
			{
				ret.add(f);
			}
		}
		
		return ret;
	}


}
