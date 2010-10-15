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

package de.flapdoodle.mongoom.mapping.index;

import de.flapdoodle.mongoom.annotations.Direction;

public class FieldIndex
{
	final String _name;
	final Direction _direction;
	
	public FieldIndex(String name, Direction direction)
	{
		_name = name;
		_direction = direction;
	}
	
	public String name()
	{
		return _name;
	}
	
	public Direction direction()
	{
		return _direction;
	}
	
	@Override
	public String toString()
	{
		return _name+": "+_direction;
	}
}
