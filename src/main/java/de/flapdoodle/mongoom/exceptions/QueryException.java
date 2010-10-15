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

public class QueryException extends ObjectMapperException
{
	protected final String _name;
	private final Object _oldValue;
	private Object _newValue;

	public QueryException(String name, Object oldvalue, Object newvalue)
	{
		_name = name;
		_oldValue = oldvalue;
		_newValue = newvalue;
	}
	
	@Override
	public String getLocalizedMessage()
	{
		return _name+" allready set with "+_oldValue+" (new: "+_newValue+")";
	}

}
