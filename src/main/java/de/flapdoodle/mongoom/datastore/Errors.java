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

package de.flapdoodle.mongoom.datastore;

import com.mongodb.CommandResult;
import com.mongodb.DB;

import de.flapdoodle.mongoom.exceptions.ObjectMapperException;
import de.flapdoodle.mongoom.exceptions.UpdateFailedException;

public final class Errors
{
	private Errors()
	{
		
	}

	public static void checkError(DB db,Operation operation)
	{
		CommandResult lastError = db.getLastError();
		if (lastError.get("err")!=null)
		{
			DatastoreImpl._logger.severe(lastError.get("err").toString());
			// throw lastError.getException();
			throw new ObjectMapperException(lastError.get("err").toString());
		}
		switch (operation)
		{
			case Update:
				if (lastError.getInt("updatedExisting", 0)==0)
				{
					throw new UpdateFailedException();
				}
				break;
				
			case Delete:
				if (lastError.getInt("n", 0)==0)
				{
					throw new UpdateFailedException();
				}
				break;
		}
			
	}
}
