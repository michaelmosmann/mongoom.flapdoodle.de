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

package de.flapdoodle.mongoom.examples.callbacks;

import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.annotations.Id;
import de.flapdoodle.mongoom.annotations.OnRead;
import de.flapdoodle.mongoom.annotations.OnWrite;
import de.flapdoodle.mongoom.annotations.Transient;
import de.flapdoodle.mongoom.mapping.callbacks.IEntityReadCallback;
import de.flapdoodle.mongoom.mapping.callbacks.IEntityWriteCallback;
import de.flapdoodle.mongoom.types.Reference;


@Entity(value = "Stats",onRead=Stats.OnReadCallback.class,onWrite=Stats.OnWriteCallback.class)
public class Stats {
	@Id
	Reference<Stats> _id;
	
	Integer _a;
	
	Integer _b;
	
	Integer _writeC;
	
	@Transient
	Integer _readC;
	
//	@OnRead
	protected void onRead()
	{
		if ((_a!=null) && (_b!=null)) _readC=_a+_b;
	}
	
//	@OnWrite
	protected void onWrite()
	{
		if ((_a!=null) && (_b!=null)) _writeC=_a+_b;
	}

	
	public Integer getA() {
		return _a;
	}

	
	public void setA(Integer a) {
		_a = a;
	}

	
	public Integer getB() {
		return _b;
	}

	
	public void setB(Integer b) {
		_b = b;
	}

	
	public Integer getWriteC() {
		return _writeC;
	}

	
	public Integer getReadC() {
		return _readC;
	}
	
	public static class OnReadCallback implements IEntityReadCallback<Stats> {
		@Override
		public void onRead(Stats entity) {
			entity.onRead();
		}
	}
	
	public static class OnWriteCallback implements IEntityWriteCallback<Stats> {
		@Override
		public void onWrite(Stats entity) {
			entity.onWrite();
		}
	}
}
