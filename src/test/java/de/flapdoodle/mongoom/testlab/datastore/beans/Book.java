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

package de.flapdoodle.mongoom.testlab.datastore.beans;

import java.awt.Color;

import de.flapdoodle.mongoom.annotations.CappedAt;
import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.annotations.Id;
import de.flapdoodle.mongoom.annotations.Version;
import de.flapdoodle.mongoom.annotations.index.IndexGroup;
import de.flapdoodle.mongoom.annotations.index.IndexedInGroup;
import de.flapdoodle.mongoom.mapping.callbacks.IEntityWriteCallback;
import de.flapdoodle.mongoom.mapping.converter.extended.color.ColorConverterOptions;
import de.flapdoodle.mongoom.types.Reference;

@Entity(value="Book",cap=@CappedAt(size=10000,count=9),onWrite=Book.BookOnWrite.class)
@IndexGroup(group="all")
public class Book {

	@Id
	Reference<Book> _id;
	
	@Version
	String _version;
	
	@IndexedInGroup(group="all",priority=1)
	String _name;
	
	int _nameLen;
	
	@ColorConverterOptions(green=@IndexedInGroup(group="all",priority=0))
	Color _color;

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}
	
	@Override
	public String toString() {
		return "Book {name:'"+_name+"',len:"+_nameLen+",[_id:"+_id+",_version:"+_version+"]}";
	}
	
	public static class BookOnWrite implements IEntityWriteCallback<Book> {

		@Override
		public void onWrite(Book entity) {
			if (entity._name!=null) entity._nameLen=entity._name.length();
		}
		
	}
}
