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

package de.flapdoodle.mongoom.mapping.converter.generics;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

import de.flapdoodle.mongoom.exceptions.MappingException;

public class TypeExtractor
{
	// http://www.artima.com/weblogs/viewpost.jsp?thread=208860

	public static Class<?> getClass(Type type)
	{
		if (type instanceof Class)
		{
			return (Class) type;
		}
		else if (type instanceof ParameterizedType)
		{
			return getClass(((ParameterizedType) type).getRawType());
		}
		else if (type instanceof GenericArrayType)
		{
			Type componentType = ((GenericArrayType) type).getGenericComponentType();
			Class<?> componentClass = getClass(componentType);
			if (componentClass != null)
			{
				return Array.newInstance(componentClass, 0).getClass();
			}
			else
			{
				return null;
			}
		}
		else
		{
			return null;
		}
	}

	
	public static <T> Map<Type,Type> getTypeArgumentMap(Class<? extends T> childClass)
	{
		Map<Type, Type> resolvedTypes = new HashMap<Type, Type>();
		Type type = childClass;
		Class<?> baseClass=Object.class;
		while (!getClass(type).equals(baseClass))
		{
			if (type instanceof Class)
			{
				type = ((Class) type).getGenericSuperclass();
			}
			else
			{
				ParameterizedType parameterizedType = (ParameterizedType) type;
				Class<?> rawType = (Class) parameterizedType.getRawType();

				Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
				TypeVariable<?>[] typeParameters = rawType.getTypeParameters();
				for (int i = 0; i < actualTypeArguments.length; i++)
				{
					resolvedTypes.put(typeParameters[i], actualTypeArguments[i]);
				}

				if (!rawType.equals(baseClass))
				{
					type = rawType.getGenericSuperclass();
				}
			}
		}
		
		return resolvedTypes;
	}
	
 
	public static Class<?> getParameterizedClass(Class<?> declaringClass, Type genericType, final int index)
	{
		if (genericType instanceof ParameterizedType)
		{
		    ParameterizedType ptype = (ParameterizedType) genericType;
		    if ((ptype.getActualTypeArguments() != null) && (ptype.getActualTypeArguments().length <= index))
		    {
		        return null;
		    }
		    Type paramType = ptype.getActualTypeArguments()[index];
		    if (paramType instanceof GenericArrayType)
		    {
		        Class arrayType = (Class) ((GenericArrayType) paramType).getGenericComponentType();
		        return Array.newInstance(arrayType, 0).getClass();
		    }
		    else
		    {
		        if (paramType instanceof ParameterizedType)
		        {
		            ParameterizedType paramPType = (ParameterizedType) paramType;
		            return (Class) paramPType.getRawType();
		        }
		        else
		        {
		            if (paramType instanceof TypeVariable)
		            {
		            	Map<Type, Type> typeArgumentMap = getTypeArgumentMap(declaringClass);
		            	TypeVariable typeVar=(TypeVariable) paramType;
		            	Type type = typeArgumentMap.get(typeVar);
		            	if (type!=null)
		            	{
		            		return (Class<?>) type;
		            	}
		            	else
		            	{
		                throw new MappingException("Generic Typed Class not supported:  <"
		                        + ((TypeVariable) paramType).getName() + "> = "
		                        + ((TypeVariable) paramType).getBounds()[0]);
		            	}
		            }
		            else
		                if (paramType instanceof Class)
		                {
		                    return (Class) paramType;
		                }
		                else
		                {
		                    throw new MappingException(
		                            "Unknown type... pretty bad... call for help, wave your hands... yeah!");
		                }
		        }
		    }
		}
		return null;
	}

}
