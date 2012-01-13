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

import de.flapdoodle.mongoom.annotations.Property;
import de.flapdoodle.mongoom.mapping.properties.PropertyReference;

public class Tag {

	public static final PropertyReference<String> Tag=de.flapdoodle.mongoom.mapping.properties.Property.ref("tag",String.class);
	
	@Property("tag")
	String _tag;

	public static Tag getTag(String tag) {
		Tag ret = new Tag();
		ret._tag = tag;
		return ret;
	}

	public String getTag() {
		return _tag;
	}

	@Override
	public String toString() {
		return "Tag(" + _tag + ")";
	}
}
