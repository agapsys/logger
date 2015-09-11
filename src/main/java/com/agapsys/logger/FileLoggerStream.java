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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;

/**
 * File output stream for log messages
 * @author Leandro Oliveira (leandro@agapsys.com)
 */
public class FileLoggerStream implements LoggerStream {
	private final FileOutputStream fos;
	private final PrintStream printStream;
	private boolean closed = false;
	
	/**
	 * Constructor
	 * @param fileOutputStream  File output stream associated with this stream.
	 */
	public FileLoggerStream(FileOutputStream fileOutputStream) {
		this.fos = fileOutputStream;
		this.printStream = new PrintStream(fileOutputStream);
	}

	@Override
	public void println(Date localTimestamp, String logType, String msg) {
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
	
	public FileOutputStream getFileOutputStream() {
		return fos;
	}
}
