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

import java.util.List;

import de.flapdoodle.mongoom.annotations.Direction;
import de.flapdoodle.mongoom.annotations.index.IndexedInGroup;

public class MetaInfo {

	//	@Property("year")
	@IndexedInGroup(group = "docId", direction = Direction.DESC)
	int _year;

	//	@Property("category")
	List<String> _category;

	//	@Property("tags")
	List<Tag> _tags;

	public int getYear() {
		return _year;
	}

	public void setYear(int year) {
		_year = year;
	}

	public List<String> getCategory() {
		return _category;
	}

	public void setCategory(List<String> category) {
		_category = category;
	}

	public List<Tag> getTags() {
		return _tags;
	}

	public void setTags(List<Tag> tags) {
		_tags = tags;
	}

	@Override
	public String toString() {
		return "MetaInfo{year: " + _year + ",category: " + _category + ",tags: " + _tags + "}";
	}
}
