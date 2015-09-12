/*
 * Copyright 2015 Agapsys Tecnologia Ltda-ME.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.agapsys.logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import org.junit.After;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 *
 * @author Leandro Oliveira (leandro@agapsys.com)
 */
public class LoggerTest {
	// CLASS SCOPE =============================================================
	private static final Random RANDOM = new Random();
	
	public static int randInt(int min, int max) {
		int randomNum = RANDOM.nextInt((max - min) + 1) + min;

		return randomNum;
	}
	// =========================================================================
	
	// INSTANCE SCOPE ==========================================================
	private final ConsoleLoggerStream cls = new ConsoleLoggerStream() {
		@Override
		protected String getOutputMessage(Date localTimestamp, String logType, String msg) {
			return LoggerTest.this.getOutputMessage(localTimestamp, logType, msg);
		}
	};

	private Logger logger;
	private StringBufferLoggerStream sbls;
	private FileLoggerStream fls;
	private File logFile;
	
	private String getOutputMessage(Date localTimestamp, String logType, String message) {
		return String.format("[%s] %s", logType, message);
	}
	
	@Before
	public void before() throws FileNotFoundException {
		File userHome = new File(System.getProperty("user.home"));
		
		while(true) {
			String fileName = String.format("logger-test-out-%d", randInt(1, 1000));
			File tmpFile = new File(userHome, fileName);
			if (!tmpFile.exists()) {
				logFile = tmpFile;
				break;
			}
		}
		
		sbls = new StringBufferLoggerStream() {

			@Override
			protected String getOutputMessage(Date localTimestamp, String logType, String msg) {
				return LoggerTest.this.getOutputMessage(localTimestamp, logType, msg);
			}
			
		};
		fls = new FileLoggerStream(logFile, false) {

			@Override
			protected String getOutputMessage(Date localTimestamp, String logType, String msg) {
				return LoggerTest.this.getOutputMessage(localTimestamp, logType, msg);
			}
		};
		
		logger = new Logger();
		
		logger.addStream(Logger.INFO, sbls);
		logger.addStream(Logger.INFO, fls);
		logger.addStream(Logger.INFO, cls);
	}
	
	@After
	public void after() {
		sbls.close();
		fls.close();
		cls.close();
		logFile.delete();
	}
	
	@Test
	public void test() throws FileNotFoundException {
		for(int i = 0; i < 100; i++) {
			int random = randInt(0, 1000);
			logger.writeLog(Logger.INFO, "Logger test: " + random);
		}
		
		// Compare stream contents:
		String flsContetns = new Scanner(logFile).useDelimiter("\\Z").next() + "\n";
		String sblsContents = sbls.getBuffer().toString();
		assertEquals(sblsContents, flsContetns);		
	}
	// =========================================================================

	
}
