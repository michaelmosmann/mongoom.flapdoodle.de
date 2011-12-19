package de.flapdoodle.mongoom.testlab.gridfs;

import java.util.List;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

import de.flapdoodle.mongoom.AbstractMongoOMTest;

public class TestGridFS extends AbstractMongoOMTest {

	public void testFilestorage() {
		Mongo mongo = getMongo();
		DB db = mongo.getDB(getDatabaseName());
		GridFS gridFS = new GridFS(db, "docs");
		GridFSInputFile gridFsFile = gridFS.createFile(new byte[1024]);
		gridFsFile.setFilename("test.txt");
		gridFsFile.setContentType("text/text");
		gridFsFile.save();
		
		List<GridFSDBFile> files = gridFS.find(BasicDBObjectBuilder.start().get());
		assertEquals("Size",1,files.size());
		
		for (GridFSDBFile f : files) {
			System.out.println("File "+f.getFilename()+" "+f.getId()+"("+f+")");
		}
	}
}
