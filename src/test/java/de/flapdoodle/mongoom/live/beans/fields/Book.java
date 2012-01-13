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

package de.flapdoodle.mongoom.live.beans.fields;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.annotations.Property;
import de.flapdoodle.mongoom.annotations.Transient;
import de.flapdoodle.mongoom.annotations.index.IndexOption;
import de.flapdoodle.mongoom.annotations.index.Indexed;
import de.flapdoodle.mongoom.mapping.properties.PropertyReference;

@Entity(value = "Book")
public class Book extends ReferenceBean<Book> {

	public static final PropertyReference<Set<String>> Category=de.flapdoodle.mongoom.mapping.properties.Property.ref("category",de.flapdoodle.mongoom.mapping.properties.Property.setType(String.class));
	public static final PropertyReference<String> Name=de.flapdoodle.mongoom.mapping.properties.Property.ref("name",String.class);
	
	static String _doNotMap = "DO_NOT_MAP";

	@Property("name")
	@Indexed(options = @IndexOption(unique = true))
	String _name;

	@Transient
	Object _stuff;

	@Property("category")
	Set<String> _category;

	public static Book getInstance(String name, String... categories) {
		Book ret = new Book();
		ret._name = name;
		ret._category = Sets.newHashSet(categories);
		return ret;
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public Object getStuff() {
		return _stuff;
	}

	public void setStuff(Object stuff) {
		_stuff = stuff;
	}

	public Set<String> getCategory() {
		return _category;
	}

	public void setCategory(Set<String> category) {
		_category = category;
	}

}
