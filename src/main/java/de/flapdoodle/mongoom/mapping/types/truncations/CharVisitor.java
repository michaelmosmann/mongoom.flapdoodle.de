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

package de.flapdoodle.mongoom.mapping.types.truncations;

import java.util.Set;

import com.google.common.collect.Sets;

import de.flapdoodle.mongoom.mapping.ITransformation;
import de.flapdoodle.mongoom.mapping.properties.TypedPropertyName;


public class CharVisitor extends AbstractSmallerTypeVisitor<Character,String>  {

	
	public CharVisitor() {
		super(String.class, char.class,Character.class);
	}
	
	@Override
	protected ITransformation<Character, String> newTransformation() {
		return new CharTransformation();
	}

	static class CharTransformation implements ITransformation<Character, String> {

		@Override
		public String asObject(Character value) {
			return value!=null ? value.toString() : null;
		}

		@Override
		public Character asEntity(String object) {
			return (object!=null && !object.isEmpty()) ? object.charAt(0) : null;
		}

		@Override
		public <Source> ITransformation<Source, ?> propertyTransformation(TypedPropertyName<Source> property) {
			return null;
		}

		@Override
		public ITransformation<?, ?> propertyTransformation(String property) {
			return null;
		}

		@Override
		public Set<TypedPropertyName<?>> properties() {
			return Sets.newHashSet();
		}
		
	}
}
