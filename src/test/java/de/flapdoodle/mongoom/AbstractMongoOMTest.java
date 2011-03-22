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

package de.flapdoodle.mongoom;

import java.util.logging.Logger;

import junit.framework.TestCase;

import com.mongodb.Mongo;

import de.flapdoodle.embedmongo.MongoDBRuntime;
import de.flapdoodle.embedmongo.MongodExecutable;
import de.flapdoodle.embedmongo.MongodProcess;
import de.flapdoodle.embedmongo.config.MongodConfig;
import de.flapdoodle.embedmongo.distribution.Version;
import de.flapdoodle.mongoom.logging.LogConfig;

public abstract class AbstractMongoOMTest extends TestCase {

	private static final Logger _logger = LogConfig.getLogger(AbstractMongoOMTest.class);
	private MongodExecutable _mongodExe;
	private MongodProcess _mongod;

	private Mongo _mongo;
	private static final String DATABASENAME = "mongoom_test";

	@Override
	protected void setUp() throws Exception {

		MongoDBRuntime runtime = MongoDBRuntime.getDefaultInstance();
		_mongodExe = runtime.prepare(new MongodConfig(Version.V1_8_0, 12345));
		_mongod=_mongodExe.start();
		
		super.setUp();

		_mongo = new Mongo("localhost", 12345);
		_logger.severe("DB: " + _mongo.getDatabaseNames());
//
//		if (!cleanUpAfterTest()) {
//			_mongo.dropDatabase(DATABASENAME);
//		}
	}

	@Override
	protected void tearDown() throws Exception {
//		if (cleanUpAfterTest())
//			_mongo.dropDatabase(DATABASENAME);
		super.tearDown();
		
		_mongod.stop();
		_mongodExe.cleanup();
	}

	public Mongo getMongo() {
		return _mongo;
	}

	public String getDatabaseName() {
		return DATABASENAME;
	}

//	protected boolean cleanUpAfterTest() {
//		return true;
//	}
}
