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

import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Logger for multiple stream outputs. This class is intended to be thread safe
 *
 * @author Leandro Oliveira (leandro@agapsys.com)
 */
public class Logger {
	public static final String INFO    = "INFO";
	public static final String WARNING = "WARNING";
	public static final String ERROR   = "ERROR";
	private final Map<String, Set<LoggerStream>> streams = new LinkedHashMap<>();
	private Map<String, Set<LoggerStream>> readOnlyStreams = null;

	/**
	 * Adds an output stream for given log type
	 *
	 * @param logType log type
	 * @param stream stream to be associated with given log type
	 */
	public void addStream(String logType, LoggerStream stream) {
		if (logType == null || logType.isEmpty())
			throw new IllegalArgumentException("Null/Empty logType");
		
		if (stream == null)
			throw new IllegalArgumentException("Null stream");
		
		synchronized (streams) {
			Set<LoggerStream> streamSet;

			if (!streams.containsKey(logType)) {
				streamSet = new LinkedHashSet<>();
				streams.put(logType, streamSet);
			} else {
				streamSet = streams.get(logType);
			}

			if (streamSet.add(stream)) {
				readOnlyStreams = null;
			}
		}
	}

	/**
	 * Removes an stream associated with given log type.
	 *
	 * @param logType log type
	 * @param stream stream to be removed.
	 */
	public void removeStream(String logType, LoggerStream stream) {
		if (logType == null || logType.isEmpty())
			throw new IllegalArgumentException("Null/Empty logType");
		
		if (stream == null)
			throw new IllegalArgumentException("Null stream");
		
		synchronized (streams) {
			if (streams.containsKey(logType)) {
				if (streams.get(logType).remove(stream)) {
					readOnlyStreams = null;
				}
			}
		}
	}

	/**
	 * Remove all registered streams.
	 */
	public void removeAllStreams() {
		synchronized (streams) {
			boolean resetReadOnlyStreams = streams.size() > 0;

			streams.clear();
			if (resetReadOnlyStreams) {
				readOnlyStreams = null;
			}
		}
	}

	/**
	 * Remove all registered streams associated with given type.
	 *
	 * @param logType log type
	 */
	public void removeAllStreams(String logType) {
		if (logType == null || logType.isEmpty())
			throw new IllegalArgumentException("Null/Empty logType");

		synchronized (streams) {
			Set<LoggerStream> streamSet = streams.get(logType);
			if (streamSet != null) {
				boolean resetReadOnlyStreams = streamSet.size() > 0;
				streamSet.clear();
				if (resetReadOnlyStreams) {
					readOnlyStreams = null;
				}
			}
		}
	}

	/**
	 * @return a map with all registered streams.
	 */
	public Map<String, Set<LoggerStream>> getRegisteredStreams() {
		synchronized (streams) {
			if (readOnlyStreams == null) {
				Map<String, Set<LoggerStream>> map = new LinkedHashMap<>(streams);
				for (Map.Entry<String, Set<LoggerStream>> entry : map.entrySet()) {
					Set<LoggerStream> streamSet = Collections.unmodifiableSet(entry.getValue());
					entry.setValue(streamSet);
				}
				readOnlyStreams = Collections.unmodifiableMap(map);
			}
			return readOnlyStreams;
		}
	}

	/**
	 * @return Complete message to be printed.
	 * @param logType log type
	 * @param message message passed in
	 * {@linkplain Logger#writeLog(String, String)} call Default implementation
	 * just returns given message ignoring logType argument
	 */
	protected String getOutputMessage(String logType, String message) {
		return message;
	}

	/**
	 * Writes a message into all registered streams
	 *
	 * @param message message to be written
	 * @param logType log type
	 */
	public void writeLog(String logType, String message) {
		Date localTimestamp = new Date();
		
		if (logType == null || logType.isEmpty())
			throw new IllegalArgumentException("Null/Empty logType");
		
		if (message == null)
			throw new IllegalArgumentException("Null message");
		
		String tmpMessage = getOutputMessage(logType, message);
		synchronized (streams) {
			Set<LoggerStream> streamSet = streams.get(logType);
			if (streamSet != null) {
				for (LoggerStream stream : streamSet) {
					stream.println(localTimestamp, logType, tmpMessage);
				}
			}
		}
	}
	// =========================================================================
}
