package de.flapdoodle.mongoom.parser;

import java.awt.Color;

import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.annotations.Id;
import de.flapdoodle.mongoom.annotations.index.IndexGroup;
import de.flapdoodle.mongoom.annotations.index.IndexGroups;
import de.flapdoodle.mongoom.annotations.index.IndexOption;
import de.flapdoodle.mongoom.annotations.index.Indexed;
import de.flapdoodle.mongoom.annotations.index.IndexedInGroup;
import de.flapdoodle.mongoom.annotations.index.IndexedInGroups;
import de.flapdoodle.mongoom.examples.mapping.Document;
import de.flapdoodle.mongoom.mapping.converter.extended.color.ColorConverterOptions;
import de.flapdoodle.mongoom.types.Reference;


@Entity(value = "Simple")
@IndexGroups({@IndexGroup(group="colorName")})
public class SimpleBean {

	@Id
	Reference<SimpleBean> _id;

	
	@IndexedInGroup(group="colorName",priority=1)
	String _name;

	@ColorConverterOptions(red=@IndexedInGroups({@IndexedInGroup(group="colorName",priority=10)}))
	@Indexed(options=@IndexOption(unique=true))
	Color _color;

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public Color getColor() {
		return _color;
	}

	public void setColor(Color color) {
		_color = color;
	}

	public Reference<SimpleBean> getId() {
		return _id;
	}

}
