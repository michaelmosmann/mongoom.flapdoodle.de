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

package de.flapdoodle.mongoom.mapping.types;

import java.lang.reflect.Type;
import java.util.List;

import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.mapping.AbstractVisitor;
import de.flapdoodle.mongoom.mapping.ITransformation;
import de.flapdoodle.mongoom.mapping.ITypeInfo;
import de.flapdoodle.mongoom.mapping.ITypeVisitor;
import de.flapdoodle.mongoom.mapping.context.IMappingContext;
import de.flapdoodle.mongoom.mapping.context.IPropertyContext;
import de.flapdoodle.mongoom.mapping.converter.generics.TypeExtractor;
import de.flapdoodle.mongoom.mapping.typeinfo.TypeInfo;


public abstract class AbstractCollectionVisitor<C,M> extends AbstractVisitor {

	protected abstract ITransformation<C, List<M>> transformation(Type parameterizedClass, ITransformation transformation);

	public ITransformation<C, List<M>> transformation(IMappingContext mappingContext, IPropertyContext<?> propertyContext, ITypeInfo field) {
		Type parameterizedClass = TypeExtractor.getParameterizedClass(field.getDeclaringClass(), field.getGenericType(),0);
		if (parameterizedClass!=null) {
			ITypeVisitor typeVisitor=mappingContext.getVisitor(TypeInfo.ofClass(field),TypeInfo.of(field,parameterizedClass));
			if (typeVisitor==null) error(field.getDeclaringClass(),"Could not get TypeVisitor for "+parameterizedClass);
			ITransformation transformation = typeVisitor.transformation(mappingContext, propertyContext, TypeInfo.of(field,parameterizedClass));
			if (transformation==null) error(field.getDeclaringClass(),"Could not get Transformation for "+field);			
			return transformation(parameterizedClass, transformation);
		}
		throw new MappingException(field.getDeclaringClass(), "Type is not a Class: " + parameterizedClass);
	}
}
