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

import com.google.common.collect.Sets;

public class Author {

	public enum Status {
		Active,
		Online
	};

	String _name;

	String _eMail;

	Status _status;

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public String geteMail() {
		return _eMail;
	}

	public void seteMail(String eMail) {
		_eMail = eMail;
	}

	public Status getStatus() {
		return _status;
	}

	public void setStatus(Status status) {
		_status = status;
	}

	@Override
	public String toString() {
		return "Author{email: " + _eMail + ",name: " + _name + ",status: " + _status + "}";
	}
}
