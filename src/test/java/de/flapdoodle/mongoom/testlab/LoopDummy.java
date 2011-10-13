package de.flapdoodle.mongoom.testlab;

import de.flapdoodle.mongoom.annotations.Entity;

@Entity("LoopDummy")
public class LoopDummy {

	Loop _start;

	public Loop getStart() {
		return _start;
	}

	public void setStart(Loop start) {
		_start = start;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_start == null)
				? 0
				: _start.hashCode());
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
		LoopDummy other = (LoopDummy) obj;
		if (_start == null) {
			if (other._start != null)
				return false;
		} else if (!_start.equals(other._start))
			return false;
		return true;
	}

	public static class Loop {

		String _name;

		Loop _loop;

		public String getName() {
			return _name;
		}

		public void setName(String name) {
			_name = name;
		}

		public Loop getLoop() {
			return _loop;
		}

		public void setLoop(Loop loop) {
			_loop = loop;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((_loop == null)
					? 0
					: _loop.hashCode());
			result = prime * result + ((_name == null)
					? 0
					: _name.hashCode());
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
			Loop other = (Loop) obj;
			if (_loop == null) {
				if (other._loop != null)
					return false;
			} else if (!_loop.equals(other._loop))
				return false;
			if (_name == null) {
				if (other._name != null)
					return false;
			} else if (!_name.equals(other._name))
				return false;
			return true;
		}
	}
}
