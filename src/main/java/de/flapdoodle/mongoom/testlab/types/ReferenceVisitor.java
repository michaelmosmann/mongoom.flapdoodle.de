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

package de.flapdoodle.mongoom.testlab.types;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import org.bson.types.ObjectId;

import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.mapping.converter.factories.ReferenceConverter;
import de.flapdoodle.mongoom.mapping.converter.generics.TypeExtractor;
import de.flapdoodle.mongoom.testlab.ITransformation;
import de.flapdoodle.mongoom.testlab.ITypeInfo;
import de.flapdoodle.mongoom.testlab.ITypeVisitor;
import de.flapdoodle.mongoom.testlab.entities.IEntityContext;
import de.flapdoodle.mongoom.testlab.mapping.IMappingContext;
import de.flapdoodle.mongoom.testlab.mapping.IPropertyContext;
import de.flapdoodle.mongoom.types.Reference;


public class ReferenceVisitor<T> implements ITypeVisitor<Reference<T>, ObjectId>{
	
	@Override
	public ITransformation<Reference<T>, ObjectId> transformation(IMappingContext mappingContext,
			IPropertyContext<?> propertyContext, ITypeInfo field) {
		Class<?> parameterizedClass = TypeExtractor.getTypeclass(field.getDeclaringClass(), field.getGenericType());
		if (parameterizedClass !=null) return new ReferenceTransformation<T>((Class<T>) parameterizedClass);
		throw new MappingException(field.getDeclaringClass(), "Type is not a Class: " + parameterizedClass);
	}

}
