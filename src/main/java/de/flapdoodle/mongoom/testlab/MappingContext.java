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

import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.google.common.collect.Maps;

import de.flapdoodle.mongoom.logging.LogConfig;
import de.flapdoodle.mongoom.testlab.types.NativeTypeVisitor;
import de.flapdoodle.mongoom.testlab.types.PojoVisitor;
import de.flapdoodle.mongoom.testlab.types.ReferenceVisitor;
import de.flapdoodle.mongoom.testlab.types.SetVisitor;
import de.flapdoodle.mongoom.types.Reference;


public class MappingContext implements IMappingContext {
	
	private static final Logger _logger = LogConfig.getLogger(MappingContext.class);
	
	Map<Class<?>, ITypeVisitor> typeVisitors=Maps.newLinkedHashMap();
	{
		typeVisitors.put(Reference.class, new ReferenceVisitor());
		typeVisitors.put(Set.class, new SetVisitor());
		typeVisitors.put(String.class, new NativeTypeVisitor<String>(String.class));
		typeVisitors.put(Integer.class, new NativeTypeVisitor<Integer>(Integer.class));
	}
	ITypeVisitor _defaultVisitor=new PojoVisitor();
	
	@Override
	public <Type> ITypeVisitor<Type, ?> getVisitor(ITypeInfo containerType, ITypeInfo type) {
		_logger.severe("getVisitor: "+containerType+" -> "+type);
		ITypeVisitor result = typeVisitors.get(type.getType());
		if (result==null) {
			result=_defaultVisitor;
		}
		return result;
	}
}
