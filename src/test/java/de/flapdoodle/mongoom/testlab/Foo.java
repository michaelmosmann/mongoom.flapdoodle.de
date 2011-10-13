package de.flapdoodle.mongoom.testlab;

public class Foo<T> {

	String _name;

	T _value;

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public T getValue() {
		return _value;
	}

	public void setValue(T value) {
		_value = value;
	}

	@Override
	public String toString() {
		return "Foo (name: " + _name + ",value: " + _value + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_name == null)
				? 0
				: _name.hashCode());
		result = prime * result + ((_value == null)
				? 0
				: _value.hashCode());
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
		Foo other = (Foo) obj;
		if (_name == null) {
			if (other._name != null)
				return false;
		} else if (!_name.equals(other._name))
			return false;
		if (_value == null) {
			if (other._value != null)
				return false;
		} else if (!_value.equals(other._value))
			return false;
		return true;
	}

}
