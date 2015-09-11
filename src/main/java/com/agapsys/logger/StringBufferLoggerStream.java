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

import java.util.Date;

/**
 * In-memory stream for log messages.
 * Internally the stream uses an {@linkplain StringBuffer}.
 * @author Leandro Oliveira (leandro@agapsys.com)
 */
public class StringBufferLoggerStream implements LoggerStream {
	// INSTANCE SCOPE ==========================================================
	private final StringBuffer buffer = new StringBuffer();
	private boolean closed = false;
	
	@Override
	public void println(Date localTimestamp, String logType, String msg) {
		if (!closed)
			buffer.append(msg).append("\n");
	}

	@Override
	public void close() {
		closed = true;
	}
	
	/** @return the internal buffer used by this stream. */
	public StringBuffer getBuffer() {
		return buffer;
	}
}
