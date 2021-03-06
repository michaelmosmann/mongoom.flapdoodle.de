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

package de.flapdoodle.mongoom.mapping.naming;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;



public class PropertyNamingList implements IPropertyNaming {

	List<? extends IPropertyNaming> _factories;
	
	public PropertyNamingList(Collection<? extends IPropertyNaming> factories) {
		_factories=Lists.newArrayList(factories);
	}
	
	@Override
	public PropertyName name(Field fieldType, PropertyName current) {
		PropertyName ret=current;
		for (IPropertyNaming factory : _factories) {
			ret=factory.name(fieldType, ret);
		}
		return ret;
	}
}
