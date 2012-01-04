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

package de.flapdoodle.mongoom.testlab.datastore;

import java.awt.Color;
import java.util.List;

import com.google.common.collect.Lists;

import de.flapdoodle.mongoom.AbstractMongoOMTest;
import de.flapdoodle.mongoom.IDatastore;
import de.flapdoodle.mongoom.datastore.Datastore;
import de.flapdoodle.mongoom.testlab.ColorMappingContext;
import de.flapdoodle.mongoom.testlab.datastore.beans.Book;
import de.flapdoodle.mongoom.testlab.datastore.beans.ColorsBean;
import de.flapdoodle.mongoom.testlab.datastore.beans.NativeTypes;
import de.flapdoodle.mongoom.testlab.mapping.IMappingContext;
import de.flapdoodle.mongoom.testlab.mapping.IMappingContextFactory;
import de.flapdoodle.mongoom.testlab.mapping.Transformations;


public class TestDatastore extends AbstractMongoOMTest {

	public void testBooks() {
		List<Class<?>> classes=Lists.newArrayList();
		classes.add(Book.class);
		IMappingContextFactory<?> factory=new IMappingContextFactory<IMappingContext>() {
			@Override
			public IMappingContext newContext() {
				return new ColorMappingContext();
			}
		};
		Transformations transformations = new Transformations(factory,classes);
		IDatastore datastore=new Datastore(getMongo(), getDatabaseName(), transformations);
		datastore.ensureCaps();
		datastore.ensureIndexes();
		
		Book book = new Book();
		book.setName("Bla");
		datastore.save(book);
		book.setName("Blu");
		datastore.update(book);
		book=new Book();
		book.setName("2. Buch");
		datastore.save(book);
		
		List<Book> books = datastore.find(Book.class);
		assertEquals("Size",2,books.size());
//		System.out.println("Books: "+books);
		
		books=datastore.with(Book.class).field("name").eq("Blu").result().asList();
		assertEquals("Size",1,books.size());

		for (int i=0;i<10;i++) {
			book=new Book();
			book.setName(i+". Buch");
			datastore.save(book);
		}

		books=datastore.with(Book.class).result().order("name", false).asList();
		System.out.println("Books: "+books);
		assertEquals("Size",9,books.size());
		assertEquals("9. Buch","9. Buch",books.get(0).getName());
	}
	

	public void testColors() {
		List<Class<?>> classes=Lists.newArrayList();
		classes.add(ColorsBean.class);
		IMappingContextFactory<?> factory=new IMappingContextFactory<IMappingContext>() {
			@Override
			public IMappingContext newContext() {
				return new ColorMappingContext();
			}
		};
		Transformations transformations = new Transformations(factory,classes);
		IDatastore datastore=new Datastore(getMongo(), getDatabaseName(), transformations);
		datastore.ensureCaps();
		datastore.ensureIndexes();
		
		ColorsBean colors = new ColorsBean();
		colors.setColors(Lists.newArrayList(new Color(103,53,23,43),new Color(206,106,56,86)));
		datastore.save(colors);
		colors.setColors(Lists.newArrayList(new Color(100,50,25,44),new Color(200,100,50,88)));
		datastore.update(colors);
		colors=new ColorsBean();
		colors.setColors(Lists.newArrayList(new Color(100,50,25,44)));
		datastore.save(colors);
		
		List<ColorsBean> list= datastore.find(ColorsBean.class);
		assertEquals("Size",2,list.size());
//		System.out.println("Books: "+books);
		
		list=datastore.with(ColorsBean.class).field("l","r").eq(100).result().asList();
		assertEquals("Size",2,list.size());
		
		list=datastore.with(ColorsBean.class).field("l","r").eq(200).result().asList();
		assertEquals("Size",1,list.size());
	}

	public void testNativeTypes() {
		List<Class<?>> classes=Lists.newArrayList();
		classes.add(NativeTypes.class);
		IMappingContextFactory<?> factory=new IMappingContextFactory<IMappingContext>() {
			@Override
			public IMappingContext newContext() {
				return new ColorMappingContext();
			}
		};
		Transformations transformations = new Transformations(factory,classes);
		IDatastore datastore=new Datastore(getMongo(), getDatabaseName(), transformations);
		datastore.ensureCaps();
		datastore.ensureIndexes();
		
		NativeTypes nt = NativeTypes.withValues();
		NativeTypes nt2 = NativeTypes.withValues();
		assertEquals("Eq",nt, nt2);
		
		datastore.save(nt);
		List<NativeTypes> nts = datastore.find(NativeTypes.class);
		assertEquals("Size",1,nts.size());
		
		NativeTypes read=datastore.with(NativeTypes.class).id().eq(nt.getId()).result().get();
		assertEquals("Eq",read, nt);
	}
	
}
