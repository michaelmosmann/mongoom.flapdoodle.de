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

/**
 * 
 */
package de.flapdoodle.mongoom.datastore.factories;

import java.util.List;

import com.mongodb.DBObject;

import de.flapdoodle.collections.Lists;
import de.flapdoodle.mongoom.exceptions.MappingException;
import de.flapdoodle.mongoom.exceptions.QueryException;

public class OrObjectFactory implements IDBObjectFactory
{
	private final IDBObjectFactory _parent;

	private List<IDBObjectFactory> _list;

	private DBObjectFactory _current;

	public OrObjectFactory(IDBObjectFactory parent)
	{
		_parent = parent;
		_list = Lists.newArrayList();

		Object olist = _parent.getValue("$or");
		if ((olist == null) || (olist instanceof List))
		{
			if (olist != null)
			{
				_list = (List<IDBObjectFactory>) olist;
			}
			else
			{
				_parent.set("$or", _list);
			}
		}
		else
		{
			throw new QueryException("$or", olist, _list);
		}
		_current = DBObjectFactory.start();
		_list.add(_current);
	}

	@Override
	public DBObject get()
	{
		throw new MappingException("should never called");
		// return _parent.get();
	}

	@Override
	public Object getValue(String name)
	{
		throw new MappingException("should never called");
	}

	@Override
	public IDBObjectFactory get(String name)
	{
		return _current.get(name);
	}

	@Override
	public IDBObjectFactory set(String name, Object value)
	{
		_current.set(name, value);
		return this;
	}
}