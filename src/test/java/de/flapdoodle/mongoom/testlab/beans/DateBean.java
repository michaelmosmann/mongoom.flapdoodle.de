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
import java.util.Date;

import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.annotations.Id;
import de.flapdoodle.mongoom.annotations.Property;
import de.flapdoodle.mongoom.annotations.Views;
import de.flapdoodle.mongoom.annotations.index.IndexGroup;
import de.flapdoodle.mongoom.annotations.index.Indexed;
import de.flapdoodle.mongoom.annotations.index.IndexedInGroup;
import de.flapdoodle.mongoom.mapping.types.date.DateMappingOptions;
import de.flapdoodle.mongoom.types.Reference;

@Entity("DateBean")
@Views(DateBean.DateView.class)
@IndexGroup(group="year")
public class DateBean {

	@Id
	Reference<DateBean> _id;
	
	@Property("d")
	@Indexed
	@DateMappingOptions(year={@IndexedInGroup(group="year")})
	Date _date;
	
	public Date getDate() {
		return _date;
	}
	
	public void setDate(Date date) {
		_date = date;
	}
	
	public Reference<DateBean> getId() {
		return _id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_date == null)
				? 0
				: _date.hashCode());
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
		DateBean other = (DateBean) obj;
		if (_date == null) {
			if (other._date != null)
				return false;
		} else if (!_date.equals(other._date))
			return false;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		return true;
	}


	public static class DateView {

		@Property("d.y")
		int _year;

		@Property("d")
		Date _date;

		public int getYear() {
			return _year;
		}
		
		public Date getDate() {
			return _date;
		}

	}
}