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
import java.io.FileOutputStream;
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
	private final ConsoleLoggerStream cls = ConsoleLoggerStream.getSingletonInstance();

	private Logger logger;
	private StringBufferLoggerStream sbls;
	private FileLoggerStream fls;
	private File logFile;
	
	@Before
	public void before() throws FileNotFoundException {
		logFile = new File("log.txt");
		
		sbls = new StringBufferLoggerStream();
		fls = new FileLoggerStream(new FileOutputStream(logFile));
		
		logger = new Logger() {

			@Override
			protected String getOutputMessage(LogType logType, String message) {
				return String.format("[%s] %s", logType.name(), message);
			}
			
		};
		
		logger.addStream(LogType.INFO, sbls);
		logger.addStream(LogType.INFO, fls);
		logger.addStream(LogType.INFO, cls);
	}
	
	@After
	public void after() {
		sbls.close();
		fls.close();
		cls.close();
	}
	
	@Test
	public void test() throws FileNotFoundException {
		for(int i = 0; i < 100; i++) {
			int random = randInt(0, 1000);
			logger.writeLog(LogType.INFO, "Logger test: " + random);
		}
		
		// Compare stream contents:
		String flsContetns = new Scanner(logFile).useDelimiter("\\Z").next() + "\n";
		String sblsContents = sbls.getBuffer().toString();
		assertEquals(sblsContents, flsContetns);		
	}
	// =========================================================================

	
}
