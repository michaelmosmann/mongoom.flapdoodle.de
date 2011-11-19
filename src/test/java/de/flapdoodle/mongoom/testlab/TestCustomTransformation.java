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

package de.flapdoodle.mongoom.testlab;

import java.awt.Color;
import java.util.Set;

import junit.framework.TestCase;

import com.mongodb.DBObject;

import de.flapdoodle.mongoom.testlab.beans.ColorBean;
import de.flapdoodle.mongoom.testlab.beans.ColorBean.ColorView;
import de.flapdoodle.mongoom.testlab.beans.FlipFlopDummy;
import de.flapdoodle.mongoom.testlab.entities.EntityVisitor;
import de.flapdoodle.mongoom.testlab.mapping.IMappingContext;
import de.flapdoodle.mongoom.testlab.properties.Property;
import de.flapdoodle.mongoom.testlab.properties.TypedPropertyName;


public class TestCustomTransformation extends TestCase {

	public void testFlipFlop() {
		IMappingContext mappingContext = new ColorMappingContext();
		EntityVisitor<ColorBean> entityVisitor = new EntityVisitor<ColorBean>();
		IEntityTransformation<ColorBean> transformation = entityVisitor.transformation(mappingContext, ColorBean.class);
		assertNotNull(transformation);
		ColorBean dummy = new ColorBean();
		dummy.setColor(new Color(200,100,50,88));
		DBObject dbObject = transformation.asObject(dummy);
		System.out.println("DBObject:" + dbObject);
		ColorBean read = transformation.asEntity(dbObject);
		System.out.println("DBObject:" + read);
		assertEquals("Eq", dummy, read);

		ITransformation<Color, DBObject> colorTrans = (ITransformation<Color, DBObject>) transformation.propertyTransformation(TypedPropertyName.of("c", Color.class));
		Color sourceColor = new Color(1,2,3,4);
		DBObject colorAsObject = colorTrans.asObject(sourceColor);
		System.out.println("DBObject.Color:" + colorAsObject);
		Color color = colorTrans.asEntity(colorAsObject);
		assertEquals("Eq", sourceColor, color);
		
		ITransformation<Integer, Object> rtrans = (ITransformation<Integer, Object>) colorTrans.propertyTransformation(TypedPropertyName.of("r",Integer.class));
		Integer value=12;
		Object object = rtrans.asObject(value);
		Integer propertyValue = rtrans.asEntity(object);
		assertEquals("Eq", value, propertyValue);
		
		ITransformation<ColorView, DBObject> viewTransformation = transformation.viewTransformation(ColorBean.ColorView.class);
		dbObject = transformation.asObject(dummy);
		ColorView colorView = viewTransformation.asEntity(dbObject);
		assertEquals("Eq.Red", dummy.getColor().getRed(),colorView.getRed());
		assertEquals("Eq.Color", dummy.getColor(),colorView.getColor());
	}
}
