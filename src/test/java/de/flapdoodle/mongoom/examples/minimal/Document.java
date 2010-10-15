package de.flapdoodle.mongoom.examples.minimal;

import org.bson.types.ObjectId;

import de.flapdoodle.mongoom.annotations.Entity;
import de.flapdoodle.mongoom.annotations.Id;

@Entity("Document")
public class Document
{
	@Id
	ObjectId _id;
}
