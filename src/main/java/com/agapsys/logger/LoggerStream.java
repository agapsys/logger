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
 * Represents an output stream for log messages
 * @author Leandro Oliveira (leandro@agapsys.com)
 */
public interface LoggerStream {
	/** 
	 * Print a message into stream
	 * @param localTimestamp local timestamp
	 * @param msg message to be printed
	 * @param logType log type
	 */
	void println(Date localTimestamp, String logType, String msg);
		
	/** Closes the stream. */
	void close();
}
