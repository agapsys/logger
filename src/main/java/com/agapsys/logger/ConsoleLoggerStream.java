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

import java.io.PrintStream;

/**
 * Console output stream for log messages.
 * @author Leandro Oliveira (leandro@agapsys.com)
 */
public class ConsoleLoggerStream extends TextLoggerStream {
	// CLASS SCOPE =============================================================
	private static final PrintStream out = System.out;
	
	private static ConsoleLoggerStream singletonInstance;
	private static boolean closed = false;
	
	/** @return A singleton instance. */
	public static ConsoleLoggerStream getSingletonInstance() {
		if (singletonInstance == null)
			singletonInstance = new ConsoleLoggerStream();
		
		return singletonInstance;
	}
	// =========================================================================
	
	// INSTANCE SCOPE ==========================================================
	@Override
	protected void println(String msg) {
		if (!closed)
			out.println(msg);
	}

	@Override
	public void close() {
		closed = true;
	}
	// =========================================================================
}
