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

package de.flapdoodle.mongoom.mapping.types.color;

import de.flapdoodle.mongoom.mapping.properties.PropertyReference;


public class Colors {
	public static final PropertyReference<Integer> Red=PropertyReference.of("r",Integer.class);
	public static final PropertyReference<Integer> Green=PropertyReference.of("g",Integer.class);
	public static final PropertyReference<Integer> Blue=PropertyReference.of("b",Integer.class);
	public static final PropertyReference<Integer> Alpha=PropertyReference.of("a",Integer.class);
}
