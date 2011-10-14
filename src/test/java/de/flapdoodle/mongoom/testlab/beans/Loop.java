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

public class Loop {

	String _name;

	Loop _loop;

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public Loop getLoop() {
		return _loop;
	}

	public void setLoop(Loop loop) {
		_loop = loop;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_loop == null)
				? 0
				: _loop.hashCode());
		result = prime * result + ((_name == null)
				? 0
				: _name.hashCode());
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
		Loop other = (Loop) obj;
		if (_loop == null) {
			if (other._loop != null)
				return false;
		} else if (!_loop.equals(other._loop))
			return false;
		if (_name == null) {
			if (other._name != null)
				return false;
		} else if (!_name.equals(other._name))
			return false;
		return true;
	}
}