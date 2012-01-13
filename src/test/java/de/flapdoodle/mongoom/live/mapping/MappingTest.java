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

package de.flapdoodle.mongoom.live.mapping;

import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;

import de.flapdoodle.mongoom.AbstractDatastoreTest;
import de.flapdoodle.mongoom.IDatastore;
import de.flapdoodle.mongoom.IEntityQuery;
import de.flapdoodle.mongoom.live.beans.Author;
import de.flapdoodle.mongoom.live.beans.Author.Status;
import de.flapdoodle.mongoom.live.beans.Document;
import de.flapdoodle.mongoom.live.beans.Log;
import de.flapdoodle.mongoom.live.beans.MetaInfo;
import de.flapdoodle.mongoom.live.beans.Tag;
import de.flapdoodle.mongoom.live.beans.User;
import de.flapdoodle.mongoom.live.beans.views.UsernameEmailView;

public class MappingTest extends AbstractDatastoreTest {

	
	public MappingTest() {
		super(Document.class,User.class,Log.class);
	}
	
	public void testMapping() {
//		ObjectMapper mongoom = new ObjectMapper();
//		mongoom.map(Document.class);
//		mongoom.map(User.class);
//		mongoom.map(Log.class);
//
//		IDatastore datastore = mongoom.createDatastore(getMongo(), getDatabaseName());
//
//		datastore.ensureCaps();
//		datastore.ensureIndexes();
		IDatastore datastore = getDatastore();

		Document d = new Document();
		d.setName("Doc1");
		Author a = new Author();
		a.seteMail("au@verlag.de");
		a.setName("klaus");
		a.setStatus(Status.Online);
		d.setAuthor(a);
		MetaInfo m = new MetaInfo();
		m.setCategory(Lists.newArrayList("fun", "sport"));
		m.setYear(2010);
		m.setTags(Lists.newArrayList(Tag.getTag("sommer"), Tag.getTag("winter")));
		d.setMeta(m);

		datastore.insert(d);
		System.out.println("Document: " + d.getId());

		//		datastore.save(d);
		Log log = new Log("Some Info");
		datastore.insert(log);
		System.out.println("Log: " + log.getId());

		List<Log> list = datastore.find(Log.class);
		assertEquals("Size", 1, list.size());

		List<Document> documents = datastore.find(Document.class);
		assertEquals("Size", 1, documents.size());

		assertEquals("Klaus", "klaus", documents.get(0).getAuthor().getName());

		documents = datastore.with(Document.class).field(Document.Name).eq("Doc1").result().asList();
		assertEquals("Size", 1, documents.size());

		documents = datastore.with(Document.class).field(Document.Meta).field(MetaInfo.Year).eq(2010).result().asList();
		assertEquals("Size", 1, documents.size());

		MetaInfo mq = new MetaInfo();
		mq.setCategory(Lists.newArrayList("fun", "sport"));
		mq.setYear(2010);
		IEntityQuery<Document> query = datastore.with(Document.class).or().field(Document.Meta).eq(mq).field(Document.Meta).field(MetaInfo.Category).eq(
				"fun").field(Document.Name).in("Doc1", "Doc2", "Doc3").parent().or().field(Document.Name).eq("DocX").parent().or().field(
				Document.Meta).field(MetaInfo.Year).not().type(Date.class).parent().or().field(Document.Meta).field(MetaInfo.Tags).elemMatch().field(Tag.Tag).eq("sommer").parent().parent();

		//		query.field("meta.tags").elemMatch().field("tag").eq("sommer");
		documents = query.result().asList();
		assertEquals("Size", 1, documents.size());

		System.out.println("Documents: " + documents);

		documents = datastore.with(Document.class).or().field(Document.Meta).field(MetaInfo.Tags).elemMatch().field(Tag.Tag).eq("sommer").parent().parent().result().asList();
		System.out.println("Documents: " + documents);
		assertEquals("Size", 1, documents.size());

		User user = User.getInstance("klaus");
		user.setEmail("ich@du.de");
		datastore.insert(user);
		System.out.println("Username: " + user.getUsername());
		System.out.println("Username: " + user.getId());

		user = User.getInstance("klaus2");
		user.setEmail("du@du.de");
		datastore.insert(user);
		System.out.println("Username: " + user.getUsername());
		System.out.println("Username: " + user.getId());

		List<UsernameEmailView> usernameEmails = datastore.with(User.class).withView(UsernameEmailView.class).order(
				"email", false).asList();
		System.out.println("Result: " + usernameEmails);

		//		user=User.getInstance("klaus");
		//		user.setEmail("nix@du.de");
		//		datastore.save(user);

		System.out.println("Ready, GO");
	}

//	@Override
//	protected boolean cleanUpAfterTest() {
//		return false;
//	}
}
