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
