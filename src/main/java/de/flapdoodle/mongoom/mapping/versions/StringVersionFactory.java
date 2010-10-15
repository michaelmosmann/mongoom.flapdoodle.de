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

package de.flapdoodle.mongoom.mapping.versions;

import java.util.UUID;

import de.flapdoodle.mongoom.mapping.IVersionFactory;

public class StringVersionFactory implements IVersionFactory<String>
{
	static int MAX_LOOPS=3;
	
	@Override
	public String newVersion(String oldVersion)
	{
		for (int i=0;i<MAX_LOOPS;i++)
		{
			String newVersion = UUID.randomUUID().toString();
			if (newVersion!=null)
			{
				if (!newVersion.equals(oldVersion)) return newVersion;
			}
		}
		return null;
	}
}
