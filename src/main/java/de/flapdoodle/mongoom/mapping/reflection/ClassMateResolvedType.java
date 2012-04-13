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

package de.flapdoodle.mongoom.mapping.reflection;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.classmate.MemberResolver;
import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.ResolvedTypeWithMembers;
import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.classmate.members.ResolvedField;
import com.google.common.base.Function;
import com.google.common.collect.Lists;


public class ClassMateResolvedType implements IResolvedType {

	private final TypeResolver _resolver;
	private final ResolvedType _type;
	private Class<?> _clazz;
	private ResolvedTypeWithMembers _memberType;

	public ClassMateResolvedType(TypeResolver resolver, Class<?> clazz) {
		this(resolver,resolver.resolve(clazz));
	}
	
	public ClassMateResolvedType(TypeResolver resolver, ResolvedType type) {
		_resolver = resolver;
		_type = type;
		_clazz=type.getErasedType();
		_memberType = new MemberResolver(resolver).resolve(type, null, null);
	}

	@Override
	public List<IResolvedType> typeParametersFor(Class<?> clazz) {
		return Lists.transform(_type.typeParametersFor(clazz),new Function<ResolvedType, IResolvedType>() {
			@Override
			public IResolvedType apply(ResolvedType input) {
				return new ClassMateResolvedType(_resolver, input);
			}
		});
	}
	
	@Override
	public Class<?> getErasedType() {
		return _type.getErasedType();
	}

	protected ResolvedType getResolvedType() {
		return _type;
	}

	@Override
	public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
		return _clazz.getAnnotation(annotationType);
	}
	
	@Override
	public List<IResolvedField> getMemberFields() {
		return Lists.transform(Lists.newArrayList(_memberType.getMemberFields()),(Function<? super ResolvedField, ? extends IResolvedField>) new Function<ResolvedField, IResolvedField>() {
			@Override
			public IResolvedField apply(ResolvedField input) {
				return new ClassMateResolvedField(ClassMateResolvedType.this,input);
			}
		});
	}

	protected TypeResolver typeResolver() {
		return _resolver;
	}
}
