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

package de.flapdoodle.mongoom.mapping.converter.annotations;

import java.lang.reflect.Field;

import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.annotations.index.IndexedInGroup;
import de.flapdoodle.mongoom.annotations.index.IndexedInGroups;
import de.flapdoodle.mongoom.exceptions.MappingException;


public class Annotations {
	private Annotations() {
		throw new IllegalArgumentException("no instance");
	}

	public static <T> void checkForOnlyOneAnnotation(Class<T> entityClass, Field f, Class<?>... annotations) {
		Class<?> last = null;
		for (Class annotation : annotations) {
			if (f.getAnnotation(annotation) != null) {
				if (last !=null)
					throw new MappingException(entityClass, "Field " + f + ": " + last + " collides with " + annotation);
				last=annotation;
			}
		}
	}

	public static void errorIfAnnotated(Class<?> objectClass, Field f, Class<?>... annotations) {
		for (Class annotation : annotations) {
			if (f.getAnnotation(annotation) != null) {
					throw new MappingException(objectClass, "Field " + f + ": " + annotation+" not allowed");
			}
		}
		
	}
	
	public static void errorIfAnnotated(IAnnotated type, Class<?>... annotations) {
		for (Class annotation : annotations) {
			if (type.getAnnotation(annotation) != null) {
					throw new MappingException(type.getType(), "" + annotation+" not allowed");
			}
		}
		
	}
}
