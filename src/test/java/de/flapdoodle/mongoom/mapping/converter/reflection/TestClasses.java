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

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import de.flapdoodle.mongoom.mapping.converter.reflection.ClassInformation;


public class TestClasses extends TestCase {

	public void testReflection() {
		Map<String,Field> fieldMap = ClassInformation.getFieldMap(ClassInformation.getFields(Dummy.class));
		assertEquals("Fields", 2,fieldMap.size());
		Field color=fieldMap.get("_color");
		assertNotNull("ColorField",color);
		List<Field> fields = ClassInformation.getFields(color.getType());
		for (Field f : fields) {
			System.out.println("Field: "+f);
		}
	}
	
	public static class Dummy {
		String _name;
		
		Color _color;
	}
}
