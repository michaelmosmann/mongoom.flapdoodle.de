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

package de.flapdoodle.mongoom.testlab.beans;

import java.awt.Color;

import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.annotations.Id;
import de.flapdoodle.mongoom.annotations.Property;
import de.flapdoodle.mongoom.annotations.Views;
import de.flapdoodle.mongoom.types.Reference;

@Entity("ColorBean")
@Views(ColorBean.ColorView.class)
public class ColorBean {

	@Id
	Reference<ColorBean> _id;
	
	@Property("c")
	Color _color;
	
	public Color getColor() {
		return _color;
	}

	public void setColor(Color color) {
		_color = color;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_color == null)
				? 0
				: _color.hashCode());
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
		ColorBean other = (ColorBean) obj;
		if (_color == null) {
			if (other._color != null)
				return false;
		} else if (!_color.equals(other._color))
			return false;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		return true;
	}


	public static class ColorView {

		@Property("c.r")
		int _red;

		@Property("c")
		Color _color;

		public Color getColor() {
			return _color;
		}

		public int getRed() {
			return _red;
		}
	}
}
