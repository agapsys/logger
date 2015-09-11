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

public abstract class TextLoggerStream implements LoggerStream {
	/**
	 * Returns the message which will be printed into stream.
	 * Default implementation just returns 'msg'.
	 * @param localTimestamp local timestamp
	 * @param logType log type
	 * @param msg message
	 * @return the message which will be printed into the stream
	 */
	protected String getOutputMessage(Date localTimestamp, String logType, String msg) {
		return msg;
	}
	
	/**
	 * Prints given message into this stream.
	 * @param msg message to be printed
	 */
	protected abstract void println(String msg);
	
	@Override
	public final void println(Date localTimestamp, String logType, String msg) {
		println(getOutputMessage(localTimestamp, logType, msg));
	}
}
