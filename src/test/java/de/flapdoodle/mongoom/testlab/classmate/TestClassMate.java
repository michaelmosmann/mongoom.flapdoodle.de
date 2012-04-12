package de.flapdoodle.mongoom.testlab.classmate;

import java.util.List;

import com.fasterxml.classmate.AnnotationConfiguration;
import com.fasterxml.classmate.AnnotationOverrides;
import com.fasterxml.classmate.MemberResolver;
import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.ResolvedTypeWithMembers;
import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.classmate.members.RawField;
import com.fasterxml.classmate.members.ResolvedField;

import de.flapdoodle.mongoom.types.Reference;

import junit.framework.TestCase;


public class TestClassMate extends TestCase {

	public void testReference() {
		
		
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
	
	static interface IQuack<T> {
		
	}
	
	static class QuackString implements IQuack<String> {
		
	}
	
	static class QuackBox {
		IQuack<String> inBox;
	}
}
