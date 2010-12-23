package de.flapdoodle.mongoom.examples.callbacks;

import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.annotations.Id;
import de.flapdoodle.mongoom.annotations.OnRead;
import de.flapdoodle.mongoom.annotations.OnWrite;
import de.flapdoodle.mongoom.annotations.Transient;
import de.flapdoodle.mongoom.types.Reference;


@Entity(value = "Stats")
public class Stats {
	@Id
	Reference<Stats> _id;
	
	Integer _a;
	
	Integer _b;
	
	Integer _writeC;
	
	@Transient
	Integer _readC;
	
	@OnRead
	protected void onRead()
	{
		if ((_a!=null) && (_b!=null)) _readC=_a+_b;
	}
	
	@OnWrite
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
	
	
}
