package de.flapdoodle.mongoom.examples.mapping;

import java.util.Date;

import de.flapdoodle.mongoom.annotations.CappedAt;
import de.flapdoodle.mongoom.annotations.Direction;
import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.annotations.Id;
import de.flapdoodle.mongoom.annotations.Property;
import de.flapdoodle.mongoom.annotations.Version;
import de.flapdoodle.mongoom.annotations.Views;
import de.flapdoodle.mongoom.annotations.index.IndexGroup;
import de.flapdoodle.mongoom.annotations.index.IndexGroups;
import de.flapdoodle.mongoom.annotations.index.IndexOption;
import de.flapdoodle.mongoom.annotations.index.Indexed;
import de.flapdoodle.mongoom.annotations.index.IndexedInGroup;
import de.flapdoodle.mongoom.types.Reference;

@Entity(value="Document",cap=@CappedAt(count=12))
@IndexGroups(@IndexGroup(group="multikey",options=@IndexOption(unique=true)))
@Views(DocumentView.class)
public class Document
{
	// Custom Mapping between ObjectId and typed Reference
	@Id
	Reference<Document> _id;
	
	// Version support
	@Version
	String _version;
	
	// Indexed in IndexGroup
	@IndexedInGroup(group="multikey",direction=Direction.ASC)
	String _name;
	
	// Indexed
	@Indexed
	Date _created;
	
	// Custom Propertyname
	@Property(value="metainfo")
	Meta _meta;

	public String getName()
	{
		return _name;
	}

	public void setName(String name)
	{
		_name = name;
	}

	public Date getCreated()
	{
		return _created;
	}

	public void setCreated(Date created)
	{
		_created = created;
	}

	public Meta getMeta()
	{
		return _meta;
	}

	public void setMeta(Meta meta)
	{
		_meta = meta;
	}

	public Reference<Document> getId()
	{
		return _id;
	}

	
}
