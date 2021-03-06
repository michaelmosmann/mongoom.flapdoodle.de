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

package de.flapdoodle.mongoom.mapping.context;

import de.flapdoodle.mongoom.mapping.ITransformation;
import de.flapdoodle.mongoom.mapping.ITypeInfo;
import de.flapdoodle.mongoom.mapping.ITypeVisitor;
import de.flapdoodle.mongoom.mapping.naming.IPropertyNaming;
import de.flapdoodle.mongoom.mapping.reflection.ITypeResolver;
import de.flapdoodle.mongoom.mapping.versions.IVersionFactory;



public interface IMappingContext {

	<Type> ITypeVisitor<Type, ?> getVisitor(ITypeInfo containerType, ITypeInfo type);

	ITransformation<?, ?> transformation(ITypeInfo field);
	
	void setTransformation(ITypeInfo field,ITransformation<?, ?> transformation);
	
	IPropertyNaming naming();
	
	IVersionFactory<?> versionFactory(ITypeInfo field);
	
	ITypeResolver typeResolver();
}
