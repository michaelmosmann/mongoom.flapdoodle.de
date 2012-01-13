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
import java.util.List;

import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.annotations.Id;
import de.flapdoodle.mongoom.annotations.Property;
import de.flapdoodle.mongoom.annotations.index.IndexGroup;
import de.flapdoodle.mongoom.annotations.index.IndexGroups;
import de.flapdoodle.mongoom.annotations.index.IndexedInGroup;
import de.flapdoodle.mongoom.mapping.properties.PropertyReference;
import de.flapdoodle.mongoom.mapping.types.color.ColorConverterOptions;
import de.flapdoodle.mongoom.types.Reference;

@Entity("ColorBean")
@IndexGroups({@IndexGroup(group = "colors")})
public class ColorsBean {

	public static final PropertyReference<List> Colors=de.flapdoodle.mongoom.mapping.properties.Property.ref("colors",List.class);
	
	@Id
	Reference<ColorsBean> _id;

	@Property("l")
	@ColorConverterOptions(red={@IndexedInGroup(group="colors")})
	List<Color> _colors;

	public List<Color> getColors() {
		return _colors;
	}

	public void setColors(List<Color> colors) {
		_colors = colors;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_colors == null)
				? 0
				: _colors.hashCode());
		result = prime * result + ((_id == null)
				? 0
				: _id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ColorsBean other = (ColorsBean) obj;
		if (_colors == null) {
			if (other._colors != null)
				return false;
		} else if (!_colors.equals(other._colors))
			return false;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		return true;
	}

}
