/**
 * Copyright (C) 2010 Michael Mosmann <michael@mosmann.de>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.flapdoodle.mongoom.live.beans.views;

import de.flapdoodle.mongoom.annotations.Id;
import de.flapdoodle.mongoom.annotations.Property;
import de.flapdoodle.mongoom.live.beans.User;
import de.flapdoodle.mongoom.types.Reference;

public class UsernameEmailView {

	@Id
	Reference<User> _id;

	@Property("username")
	String _uname;

	String _email;

	public String getUname() {
		return _uname;
	}

	public String getEmail() {
		return _email;
	}

	public Reference<User> getId() {
		return _id;
	}

	@Override
	public String toString() {
		return "" + _uname + "->" + _email;
	}
}
