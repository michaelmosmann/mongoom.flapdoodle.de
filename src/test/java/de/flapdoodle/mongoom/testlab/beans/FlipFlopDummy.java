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

import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.types.Reference;

@Entity("FlipFlopDummy")
public class FlipFlopDummy {

	Reference<Dummy> _id;

	Flip _flip;

	public Reference<Dummy> getId() {
		return _id;
	}

	public void setId(Reference<Dummy> id) {
		_id = id;
	}

	public Flip getFlip() {
		return _flip;
	}

	public void setFlip(Flip flip) {
		_flip = flip;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_flip == null)
				? 0
				: _flip.hashCode());
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
		FlipFlopDummy other = (FlipFlopDummy) obj;
		if (_flip == null) {
			if (other._flip != null)
				return false;
		} else if (!_flip.equals(other._flip))
			return false;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		return true;
	}
	
	
}
