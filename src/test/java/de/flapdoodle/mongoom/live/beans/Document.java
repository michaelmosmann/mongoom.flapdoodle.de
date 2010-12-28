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

package de.flapdoodle.mongoom.live.beans;

import org.bson.types.ObjectId;

import de.flapdoodle.mongoom.annotations.Direction;
import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.annotations.Id;
import de.flapdoodle.mongoom.annotations.Property;
import de.flapdoodle.mongoom.annotations.index.IndexGroup;
import de.flapdoodle.mongoom.annotations.index.IndexGroups;
import de.flapdoodle.mongoom.annotations.index.IndexOption;
import de.flapdoodle.mongoom.annotations.index.IndexedInGroup;

@Entity("Document")
@IndexGroups(@IndexGroup(group = "docId", options = @IndexOption(unique = true)))
public class Document {

	@Id
	ObjectId _id;

	//	@Property("name")
	@IndexedInGroup(group = "docId", direction = Direction.BOTH)
	String _name;

	//	@Property("author")
	@IndexedInGroup(group = "docId")
	Author _author;

	//	@Property("meta")
	MetaInfo _meta;

	public ObjectId getId() {
		return _id;
	}

	public void setName(String name) {
		_name = name;
	}

	public String getName() {
		return _name;
	}

	public Author getAuthor() {
		return _author;
	}

	public void setAuthor(Author author) {
		_author = author;
	}

	public MetaInfo getMeta() {
		return _meta;
	}

	public void setMeta(MetaInfo metaInfo) {
		_meta = metaInfo;
	}

	@Override
	public String toString() {
		return "Document {id: " + _id + ", name: " + _name + ", author: " + _author + ", meta: " + _meta + "}";
	}
}
