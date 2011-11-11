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

package de.flapdoodle.mongoom.mapping.callbacks;

import java.lang.reflect.Type;

import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.mapping.converter.generics.TypeExtractor;


public class Callbacks {
	private Callbacks() {
		throw new IllegalAccessError("singleton");
	}
	
	public static <C> C newInstance(Class<?> entityClass, Class<?> callbackType,Class<C> interfaze) {
		if (callbackType!=null) {
			Type genericInterface = TypeExtractor.getGenericInterface(callbackType, interfaze);
			if (genericInterface == null) {
				throw new MappingException(entityClass, callbackType.getName() + " does not implement "
						+ IEntityReadCallback.class.getName());
			}
			Type parameterType = TypeExtractor.getParameterizedClass(callbackType, genericInterface, 0);
			if (parameterType == null) {
				throw new MappingException(entityClass, callbackType.getName() + ": could not get TypeInformation");
			}
			Class<?> callbackEntityType = TypeExtractor.getClass(parameterType);
			if (callbackEntityType == null) {
				throw new MappingException(entityClass, callbackType.getName() + ": could not get Class for " + parameterType);
			}
			if (callbackEntityType.isAssignableFrom(entityClass)) {
				try {
					return (C) callbackType.newInstance();
				} catch (InstantiationException e) {
					throw new MappingException(entityClass, e);
				} catch (IllegalAccessException e) {
					throw new MappingException(entityClass, e);
				}
			}
		}
		return null;
	}

}
