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

package de.flapdoodle.mongoom.testlab.classmate;

import java.util.List;

import junit.framework.TestCase;

import com.fasterxml.classmate.AnnotationConfiguration;
import com.fasterxml.classmate.AnnotationOverrides;
import com.fasterxml.classmate.MemberResolver;
import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.ResolvedTypeWithMembers;
import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.classmate.members.ResolvedField;

import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.mapping.reflection.ClassMateTypeResolver;
import de.flapdoodle.mongoom.mapping.reflection.IResolvedField;
import de.flapdoodle.mongoom.mapping.reflection.IResolvedType;
import de.flapdoodle.mongoom.mapping.reflection.ITypeResolver;


public class TestClassMate extends TestCase {

	public void testGeneric() {
		
		TypeResolver resolver=new TypeResolver();
		ResolvedType resolvedType = resolver.resolve(QuackString.class);
		List<ResolvedType> params = resolvedType.typeParametersFor(IQuack.class);
		assertEquals(1, params.size());
		assertEquals(String.class, params.get(0).getErasedType());
		
		MemberResolver mresolver=new MemberResolver(resolver);
		AnnotationConfiguration annotationConfig=null;
		AnnotationOverrides annotationOverrides=null;
		ResolvedTypeWithMembers resolvedTypeWithMembers = mresolver.resolve(resolver.resolve(QuackBox.class), annotationConfig, annotationOverrides);
		
		ResolvedField[] memberFields = resolvedTypeWithMembers.getMemberFields();
		assertEquals(1, memberFields.length);
		
		ResolvedType inBoxType = memberFields[0].getType();
//		System.out.println("Field: "+inBoxType.getFullDescription());
		List<ResolvedType> inBoxParams = inBoxType.typeParametersFor(IQuack.class);
		assertEquals(1, inBoxParams.size());
		assertEquals(String.class, inBoxParams.get(0).getErasedType());
		
	}
	
	public void testGenericAdapter() {

		ITypeResolver resolver=new ClassMateTypeResolver();
		IResolvedType resolvedType = resolver.resolve(QuackString.class);
		List<IResolvedType> params = resolvedType.typeParametersFor(IQuack.class);
		assertEquals(1, params.size());
		assertEquals(String.class, params.get(0).getErasedType());
		
		IResolvedType resolvedTypeWithMembers = resolver.resolve(QuackBox.class);
		
		List<IResolvedField> memberFields = resolvedTypeWithMembers.getMemberFields();
		assertEquals(1, memberFields.size());
		
		IResolvedType inBoxType = memberFields.get(0).getType();
//		System.out.println("Field: "+inBoxType.getFullDescription());
		List<IResolvedType> inBoxParams = inBoxType.typeParametersFor(IQuack.class);
		assertEquals(1, inBoxParams.size());
		assertEquals(String.class, inBoxParams.get(0).getErasedType());
		
	}
	
	static interface IQuack<T> {
		
	}
	
	static class QuackString implements IQuack<String> {
		
	}
	
	@Entity("FU")
	static class QuackBox {
		IQuack<String> inBox;
	}
}
