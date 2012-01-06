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

package de.flapdoodle.mongoom.mapping.properties;

public final class PropertyName<T> implements IPropertyMappedName {

	private final String _name;
	private final String _mapped;
	private final Class<T> _type;

	public PropertyName(String name, String mapped,Class<T> type) {
		_name = name;
		_mapped = mapped;
		_type = type;
	}

	public String getName() {
		return _name;
	}

	@Override
	public String getMapped() {
		return _mapped;
	}
	

	public static <T> PropertyName<T> with(String name,String mapped,Class<T> type) {
		return new PropertyName<T>(name, mapped,type);
	}
	public static <T> PropertyName<T> with(String name,Class<T> type) {
		return new PropertyName<T>(name, name,type);
	}
}
