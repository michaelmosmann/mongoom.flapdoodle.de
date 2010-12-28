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

package de.flapdoodle.mongoom.exceptions;

public class MappingException extends ObjectMapperException {

	private final Class<?> _type;

	public MappingException(Class<?> type) {
		super();
		_type = type;
	}

	public MappingException(Class<?> type, String message, Throwable cause) {
		super(message, cause);
		_type = type;
	}

	public MappingException(Class<?> type, String message) {
		super(message);
		_type = type;
	}

	public MappingException(Class<?> type, Throwable cause) {
		super(cause);
		_type = type;
	}

	public MappingException(String message) {
		super(message);
		_type = null;
	}

	@Override
	public String getLocalizedMessage() {
		return (_type != null
				? "Type: " + _type.getName() + ","
				: "") + " Message: " + super.getLocalizedMessage();
	}
}
