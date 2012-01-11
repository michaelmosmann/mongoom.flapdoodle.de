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
import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

import com.mongodb.DBObject;

import de.flapdoodle.mongoom.mapping.IEntityTransformation;
import de.flapdoodle.mongoom.mapping.ITransformation;
import de.flapdoodle.mongoom.mapping.context.IMappingContext;
import de.flapdoodle.mongoom.mapping.entities.EntityVisitor;
import de.flapdoodle.mongoom.mapping.properties.TypedPropertyName;
import de.flapdoodle.mongoom.testlab.beans.ColorBean;
import de.flapdoodle.mongoom.testlab.beans.ColorBean.ColorView;
import de.flapdoodle.mongoom.testlab.beans.DateBean;


public class TestCustomTransformation extends TestCase {

	public void testColor() {
		IMappingContext mappingContext = new ColorMappingContext();
		EntityVisitor<ColorBean> entityVisitor = new EntityVisitor<ColorBean>();
		IEntityTransformation<ColorBean> transformation = entityVisitor.transformation(mappingContext, ColorBean.class);
		assertNotNull(transformation);
		ColorBean dummy = new ColorBean();
		dummy.setColor(new Color(200,100,50,88));
		DBObject dbObject = transformation.asObject(dummy);
		System.out.println("DBObject:" + dbObject);
		ColorBean read = transformation.asEntity(dbObject);
		System.out.println("ColorBean:" + read);
		assertEquals("Eq", dummy, read);

		ITransformation<Color, DBObject> colorTrans = (ITransformation<Color, DBObject>) transformation.propertyTransformation(transformation.propertyName(TypedPropertyName.of("color", Color.class)));
		Color sourceColor = new Color(1,2,3,4);
		DBObject colorAsObject = colorTrans.asObject(sourceColor);
		System.out.println("DBObject.Color:" + colorAsObject);
		Color color = colorTrans.asEntity(colorAsObject);
		assertEquals("Eq", sourceColor, color);
		
		ITransformation<Integer, Object> rtrans = (ITransformation<Integer, Object>) colorTrans.propertyTransformation(colorTrans.propertyName(TypedPropertyName.of("r",Integer.class)));
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
	
	public void testDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2011);
		cal.set(Calendar.MONTH, Calendar.FEBRUARY);
		cal.set(Calendar.DAY_OF_MONTH, 4);
		cal.set(Calendar.HOUR, 15);
		cal.set(Calendar.MINUTE, 30);
		cal.set(Calendar.SECOND, 45);
		cal.set(Calendar.MILLISECOND, 0);
		Date date=cal.getTime();
		
		IMappingContext mappingContext = new DateMappingContext();
		EntityVisitor<DateBean> entityVisitor = new EntityVisitor<DateBean>();
		IEntityTransformation<DateBean> transformation = entityVisitor.transformation(mappingContext, DateBean.class);
		assertNotNull(transformation);
		DateBean dummy = new DateBean();
		dummy.setDate(date);
		DBObject dbObject = transformation.asObject(dummy);
		System.out.println("DBObject:" + dbObject);
		DateBean read = transformation.asEntity(dbObject);
		System.out.println("DateBean:" + read);
		assertEquals("Eq", dummy, read);
		
		ITransformation<Date, DBObject> dateTrans = (ITransformation<Date, DBObject>) transformation.propertyTransformation(transformation.propertyName(TypedPropertyName.of("date", Date.class)));
		DBObject dateAsObject = dateTrans.asObject(date);
		System.out.println("DBObject.Date:" + dateAsObject);
		Date readDate = dateTrans.asEntity(dateAsObject);
		assertEquals("Eq", date, readDate);
		
		ITransformation<Integer, Object> rtrans = (ITransformation<Integer, Object>) dateTrans.propertyTransformation(dateTrans.propertyName(TypedPropertyName.of("year",Integer.class)));
		Integer value=1973;
		Object object = rtrans.asObject(value);
		Integer propertyValue = rtrans.asEntity(object);
		assertEquals("Eq", value, propertyValue);

	}
}
