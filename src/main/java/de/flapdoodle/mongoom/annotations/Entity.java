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

package de.flapdoodle.mongoom.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.flapdoodle.mongoom.mapping.callbacks.IEntityReadCallback;
import de.flapdoodle.mongoom.mapping.callbacks.IEntityWriteCallback;
import de.flapdoodle.mongoom.mapping.callbacks.NoopReadCallback;
import de.flapdoodle.mongoom.mapping.callbacks.NoopWriteCallback;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target( {ElementType.TYPE})
public @interface Entity {

	String value();

	CappedAt cap() default @CappedAt;
	
	Class<? extends IEntityReadCallback<?>> onRead() default NoopReadCallback.class;
	Class<? extends IEntityWriteCallback<?>> onWrite() default NoopWriteCallback.class;
}
