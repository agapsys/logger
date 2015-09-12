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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * File output stream for log messages
 * @author Leandro Oliveira (leandro@agapsys.com)
 */
public class FileLoggerStream extends TextLoggerStream {
	private final File outputFile;
	private final PrintStream printStream;
	private boolean closed = false;
	
	/**
	 * Constructor
	 * @param outputFile outputFile  File which will be used by this stream.
	 * @param append defines if given file shall be open for append
	 */
	public FileLoggerStream(File outputFile, boolean append) {
		try {
			FileOutputStream fos = new FileOutputStream(outputFile, append);
			this.outputFile = outputFile;
			this.printStream = new PrintStream(fos);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Constructor
	 * @param outputFile outputFile File which will be used by this stream (will be open for append).
	 */
	public FileLoggerStream(File outputFile) {
		this(outputFile, true);
	}

	@Override
	protected void println(String msg) {
		if (!closed) {
			try {
				printStream.write((msg + "\n").getBytes());
			} catch (IOException e) {
				closed = true;
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public void close() {
		printStream.close();
		closed = true;
	}
	
	public File getOutputFile() {
		return outputFile;
	}
}
