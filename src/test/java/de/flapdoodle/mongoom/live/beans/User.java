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

import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.annotations.Id;
import de.flapdoodle.mongoom.annotations.Property;
import de.flapdoodle.mongoom.annotations.Version;
import de.flapdoodle.mongoom.annotations.Views;
import de.flapdoodle.mongoom.annotations.index.IndexOption;
import de.flapdoodle.mongoom.annotations.index.Indexed;
import de.flapdoodle.mongoom.live.beans.views.UsernameEmailView;
import de.flapdoodle.mongoom.types.Reference;

@Entity("User")
@Views(UsernameEmailView.class)
public class User
{
	enum Status { Active, Online };
	
	@Id
	Reference<User> _id;
	
	@Property("username")
	@Indexed(options=@IndexOption(unique=true))
	String _username;
	
	@Property("email")
	String _email;
	
	@Property("status")
	Status _status;
	
	@Version
	String _version;
	
	protected User()
	{
		
	}
	
	public static User getInstance(String username)
	{
		User ret=new User();
		ret._username=username;
		return ret;
	}
	
	public Reference<User> getId()
	{
		return _id;
	}
	
	public String getEmail()
	{
		return _email;
	}

	public void setEmail(String email)
	{
		_email = email;
	}
	
	public Status getStatus()
	{
		return _status;
	}
	
	public void setStatus(Status status)
	{
		_status = status;
	}

	public String getUsername()
	{
		return _username;
	}
	
	
}
