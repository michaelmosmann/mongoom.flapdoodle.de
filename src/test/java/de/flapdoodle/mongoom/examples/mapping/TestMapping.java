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

package de.flapdoodle.mongoom.examples.mapping;

import java.util.Date;
import java.util.List;

import com.google.inject.internal.Lists;

import de.flapdoodle.mongoom.AbstractMongoOMTest;
import de.flapdoodle.mongoom.IDatastore;
import de.flapdoodle.mongoom.ObjectMapper;

public class TestMapping extends AbstractMongoOMTest {

	public void testDocument() {
		ObjectMapper mongoom = new ObjectMapper();
		mongoom.map(Document.class);

		IDatastore datastore = mongoom.createDatastore(getMongo(), getDatabaseName());

		datastore.ensureCaps();
		datastore.ensureIndexes();

		Document doc = new Document();
		doc.setCreated(new Date());
		doc.setName("Document One");
		Meta metaInfo = new Meta();
		metaInfo.setKeywords(Lists.newArrayList("Document", "One"));
		doc.setMeta(metaInfo);
		datastore.save(doc);

		List<Document> list = datastore.with(Document.class).result().asList();
		assertEquals("One", 1, list.size());

		List<DocumentView> views = datastore.with(Document.class).field("metainfo.keywords").eq("One").withView(
				DocumentView.class).asList();
		DocumentView view = views.get(0);
		List<String> keywords = view.getKeywords();
		assertEquals("Keywords", true, keywords.contains("One"));
	}

	@Override
	protected boolean cleanUpAfterTest() {
		return false;
	}
}
