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

package de.flapdoodle.mongoom.examples.mapping.complex;

import java.awt.Color;

import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.annotations.Id;
import de.flapdoodle.mongoom.annotations.index.IndexGroup;
import de.flapdoodle.mongoom.annotations.index.IndexGroups;
import de.flapdoodle.mongoom.annotations.index.IndexOption;
import de.flapdoodle.mongoom.annotations.index.Indexed;
import de.flapdoodle.mongoom.annotations.index.IndexedInGroup;
import de.flapdoodle.mongoom.annotations.index.IndexedInGroups;
import de.flapdoodle.mongoom.examples.mapping.Document;
import de.flapdoodle.mongoom.mapping.converter.extended.color.ColorConverterOptions;
import de.flapdoodle.mongoom.types.Reference;

@Entity(value = "ColorDocument")
@IndexGroups({@IndexGroup(group="colorName")})
public class ColorDocument {

	@Id
	Reference<Document> _id;

	
	@IndexedInGroup(group="colorName",priority=1)
	String _name;

	@ColorConverterOptions(indexed=@Indexed(options=@IndexOption(unique=true)),red=@IndexedInGroups({@IndexedInGroup(group="colorName",priority=10)}))
	Color _color;

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public Color getColor() {
		return _color;
	}

	public void setColor(Color color) {
		_color = color;
	}

	public Reference<Document> getId() {
		return _id;
	}
}
