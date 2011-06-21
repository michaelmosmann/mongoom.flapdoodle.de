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

package de.flapdoodle.mongoom.mapping.converter.extended.color;

import java.awt.Color;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import de.flapdoodle.mongoom.mapping.ITypeConverter;
import de.flapdoodle.mongoom.mapping.converter.annotations.IAnnotated;
import de.flapdoodle.mongoom.mapping.converter.annotations.NothingAnnotated;
import de.flapdoodle.mongoom.mapping.converter.factories.RawConverter;
import de.flapdoodle.mongoom.mapping.index.IndexDef;


public class ColorConverter implements ITypeConverter<Color> {

	private static RawConverter<Integer> _convertColorPart;
	
	public ColorConverter(IAnnotated annotations) {
		ColorConverterOptions colorOptions = annotations.getAnnotation(ColorConverterOptions.class);
		_convertColorPart = new RawConverter<Integer>(Integer.class, new NothingAnnotated());
	}
	
	@Override
	public ITypeConverter<?> converter(String field) {
		if (field.equals("r") || (field.equals("g") || field.equals("b"))) return _convertColorPart;
		return null;
	}

	@Override
	public Object convertTo(Color value) {
		BasicDBObject ret = new BasicDBObject();
		ret.put("r", _convertColorPart.convertTo(value.getRed()));
		ret.put("g", _convertColorPart.convertTo(value.getGreen()));
		ret.put("b", _convertColorPart.convertTo(value.getBlue()));
		return ret;
	}

	@Override
	public Color convertFrom(Object value) {
		if (value instanceof DBObject) {
			DBObject dbobject = (DBObject) value;
			Integer r=_convertColorPart.convertFrom(dbobject.get("r"));
			Integer g=_convertColorPart.convertFrom(dbobject.get("g"));
			Integer b=_convertColorPart.convertFrom(dbobject.get("b"));
			
			if ((r!=null) && (g!=null) && (b!=null)) {
				return new Color(r,g,b);
			}
		}
		return null;
	}

	@Override
	public List<IndexDef> getIndexes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean matchType(Class<?> entityClass, Class<?> type, Type genericType) {
		return type.isAssignableFrom(Color.class);
	}
	
}
