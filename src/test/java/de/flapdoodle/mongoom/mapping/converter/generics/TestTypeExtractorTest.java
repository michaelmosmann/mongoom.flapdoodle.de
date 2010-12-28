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

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;
import de.flapdoodle.mongoom.live.beans.fields.Book;
import de.flapdoodle.mongoom.types.Reference;

public class TestTypeExtractorTest extends TestCase {

	Set<Reference<Book>> _books;

	public void testFieldType() throws SecurityException, NoSuchFieldException {
		Map<Type, Type> typeMap = TypeExtractor.getTypeArgumentMap(/* Object.class, */Book.class);
		System.out.println("Types: " + typeMap);

		Field field = getClass().getDeclaredField("_books");

		Type parameterizedClass = TypeExtractor.getParameterizedClass(getClass(), field.getGenericType(), 0);
		System.out.println("ParamClass: " + parameterizedClass);
	}
}
