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

package de.flapdoodle.mongoom.testlab.datastore.beans;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import org.bson.types.Code;

import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.annotations.Id;
import de.flapdoodle.mongoom.types.Reference;


@Entity(value="NativeTypes")
public class NativeTypes {
	
	@Id
	Reference<NativeTypes> _id;

	boolean _boval;
	Boolean _bovalue;
	
	byte[] _bavalue;
	
	byte _bval;
	Byte _bvalue;
	
	short _sval;
	Short _svalue;
	
	int _ival;
	Integer _ivalue;
	
	long _lval;
	Long _lvalue;
	
	float _fval;
	Float _fvalue;
	
	double _dval;
	Double _dvalue;
	
	char _cval;
	Character _cvalue;
	
	String _val;
	
	Date _dtvalue;
	
	Pattern _pvalue;
	
	Code _covalue;
	
	public Reference<NativeTypes> getId() {
		return _id;
	}


	public static NativeTypes withValues() {
		NativeTypes ret = new NativeTypes();
		ret._boval=false;
		ret._bovalue=Boolean.TRUE;
		ret._bval=12;
		ret._bvalue=24;
		ret._cval='a';
		ret._cvalue='C';
		ret._dval=1.1d;
		ret._dvalue=2.2d;
		ret._fval=1.2f;
		ret._fvalue=2.4f;
		ret._ival=123456;
		ret._ivalue=2345678;
		ret._lval=Long.MAX_VALUE;
		ret._lvalue=Long.MIN_VALUE;
		ret._sval=4321;
		ret._svalue=8642;
		ret._val="BluBla";
		ret._dtvalue=new Date(0);
		ret._bavalue=new byte[] {0x01,0x02,0x04,0x08};
		ret._pvalue=Pattern.compile("a?a");
		ret._covalue=new Code("{/**/}");
		return ret;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(_bavalue);
		result = prime * result + (_boval
				? 1231
				: 1237);
		result = prime * result + ((_bovalue == null)
				? 0
				: _bovalue.hashCode());
		result = prime * result + _bval;
		result = prime * result + ((_bvalue == null)
				? 0
				: _bvalue.hashCode());
		result = prime * result + ((_covalue == null)
				? 0
				: _covalue.hashCode());
		result = prime * result + _cval;
		result = prime * result + ((_cvalue == null)
				? 0
				: _cvalue.hashCode());
		result = prime * result + ((_dtvalue == null)
				? 0
				: _dtvalue.hashCode());
		long temp;
		temp = Double.doubleToLongBits(_dval);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((_dvalue == null)
				? 0
				: _dvalue.hashCode());
		result = prime * result + Float.floatToIntBits(_fval);
		result = prime * result + ((_fvalue == null)
				? 0
				: _fvalue.hashCode());
		result = prime * result + ((_id == null)
				? 0
				: _id.hashCode());
		result = prime * result + _ival;
		result = prime * result + ((_ivalue == null)
				? 0
				: _ivalue.hashCode());
		result = prime * result + (int) (_lval ^ (_lval >>> 32));
		result = prime * result + ((_lvalue == null)
				? 0
				: _lvalue.hashCode());
//		result = prime * result + ((_pvalue == null)
//				? 0
//				: _pvalue.hashCode());
		result = prime * result + _sval;
		result = prime * result + ((_svalue == null)
				? 0
				: _svalue.hashCode());
		result = prime * result + ((_val == null)
				? 0
				: _val.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NativeTypes other = (NativeTypes) obj;
		if (!Arrays.equals(_bavalue, other._bavalue))
			return false;
		if (_boval != other._boval)
			return false;
		if (_bovalue == null) {
			if (other._bovalue != null)
				return false;
		} else if (!_bovalue.equals(other._bovalue))
			return false;
		if (_bval != other._bval)
			return false;
		if (_bvalue == null) {
			if (other._bvalue != null)
				return false;
		} else if (!_bvalue.equals(other._bvalue))
			return false;
		if (_covalue == null) {
			if (other._covalue != null)
				return false;
		} else if (!_covalue.equals(other._covalue))
			return false;
		if (_cval != other._cval)
			return false;
		if (_cvalue == null) {
			if (other._cvalue != null)
				return false;
		} else if (!_cvalue.equals(other._cvalue))
			return false;
		if (_dtvalue == null) {
			if (other._dtvalue != null)
				return false;
		} else if (!_dtvalue.equals(other._dtvalue))
			return false;
		if (Double.doubleToLongBits(_dval) != Double.doubleToLongBits(other._dval))
			return false;
		if (_dvalue == null) {
			if (other._dvalue != null)
				return false;
		} else if (!_dvalue.equals(other._dvalue))
			return false;
		if (Float.floatToIntBits(_fval) != Float.floatToIntBits(other._fval))
			return false;
		if (_fvalue == null) {
			if (other._fvalue != null)
				return false;
		} else if (!_fvalue.equals(other._fvalue))
			return false;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		if (_ival != other._ival)
			return false;
		if (_ivalue == null) {
			if (other._ivalue != null)
				return false;
		} else if (!_ivalue.equals(other._ivalue))
			return false;
		if (_lval != other._lval)
			return false;
		if (_lvalue == null) {
			if (other._lvalue != null)
				return false;
		} else if (!_lvalue.equals(other._lvalue))
			return false;
		if (_pvalue == null) {
			if (other._pvalue != null)
				return false;
		} else if (!comparePattern(other))
			return false;
		if (_sval != other._sval)
			return false;
		if (_svalue == null) {
			if (other._svalue != null)
				return false;
		} else if (!_svalue.equals(other._svalue))
			return false;
		if (_val == null) {
			if (other._val != null)
				return false;
		} else if (!_val.equals(other._val))
			return false;
		return true;
	}


	private boolean comparePattern(NativeTypes other) {
		return _pvalue.pattern().equals(other._pvalue.pattern()) && _pvalue.flags()==other._pvalue.flags();
	}
	
	
}
