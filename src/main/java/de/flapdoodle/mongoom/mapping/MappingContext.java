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

package de.flapdoodle.mongoom.mapping;

import java.util.Set;

import com.google.common.collect.Sets;

import de.flapdoodle.mongoom.exceptions.MappingException;

public class MappingContext<T>
{
	Set<Class> _inProgress=Sets.newHashSet();
	private Class<T> _entityClass;
	
	public MappingContext(Class<T> entityClass)
	{
		_entityClass=entityClass;
	}
	
	void mappingStart(Class type)
	{
		if (!_inProgress.add(type)) throw new MappingException(_entityClass, "Map again "+type+", Recursion detected");
	}
	
	void mappingEnd(Class type)
	{
		if (!_inProgress.remove(type)) throw new MappingException(_entityClass, "Map done for "+type+" but not started.");
	}

	public Class<T> getEntityClass()
	{
		return _entityClass;
	}
}
