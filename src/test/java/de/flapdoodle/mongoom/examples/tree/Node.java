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

package de.flapdoodle.mongoom.examples.tree;

import java.util.List;

public class Node {

	String _name;

	List<Node> _childs;

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public List<Node> getChilds() {
		return _childs;
	}

	public void setChilds(List<Node> childs) {
		_childs = childs;
	}

}
