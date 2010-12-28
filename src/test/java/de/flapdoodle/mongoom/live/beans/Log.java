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

import java.util.Date;

import org.bson.types.ObjectId;

import de.flapdoodle.mongoom.annotations.CappedAt;
import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.annotations.Id;
import de.flapdoodle.mongoom.annotations.Property;

@Entity(value = "Log", cap = @CappedAt(count = 10))
public class Log {

	@Id
	ObjectId _id;

	@Property("date")
	Date _date;

	@Property("message")
	String _message;

	protected Log() {

	}

	public Log(String message) {
		_message = message;
		_date = new Date();
	}

	public ObjectId getId() {
		return _id;
	}

	public String getMessage() {
		return _message;
	}

	public Date getDate() {
		return _date;
	}
}
