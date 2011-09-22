package de.flapdoodle.mongoom.testlab.classes;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import de.flapdoodle.mongoom.mapping.converter.reflection.ClassInformation;

import junit.framework.TestCase;


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
