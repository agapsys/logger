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
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Logger for multiple stream outputs. This class is intended to be thread safe
 *
 * @author Leandro Oliveira (leandro@agapsys.com)
 */
public class Logger {

	private final Map<LogType, Set<LoggerStream>> streams = new LinkedHashMap<>();

	private Locale locale;
	private Map<LogType, Set<LoggerStream>> readOnlyStreams = null;

	/**
	 * Constructor.
	 */
	public Logger() {
		this(Locale.getDefault());
	}

	/**
	 * Constructor.
	 *
	 * @param locale locale used in this instance.
	 */
	public Logger(Locale locale) {
		if (locale == null) {
			throw new IllegalArgumentException("Null locale");
		}

		this.locale = locale;
	}

	/**
	 * Adds an output stream for given log type
	 *
	 * @param logType log type
	 * @param stream stream to be associated with given log type
	 */
	public void addStream(LogType logType, LoggerStream stream) {
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
	public void removeStream(LogType logType, LoggerStream stream) {
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
	public void removeAllStreams(LogType logType) {
		if (logType == null) {
			throw new IllegalArgumentException("type == null");
		}

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
	public Map<LogType, Set<LoggerStream>> getRegisteredStreams() {
		synchronized (streams) {
			if (readOnlyStreams == null) {
				Map<LogType, Set<LoggerStream>> map = new LinkedHashMap<>(streams);
				for (Map.Entry<LogType, Set<LoggerStream>> entry : map.entrySet()) {
					Set<LoggerStream> streamSet = Collections.unmodifiableSet(entry.getValue());
					entry.setValue(streamSet);
				}
				readOnlyStreams = Collections.unmodifiableMap(map);
			}
			return readOnlyStreams;
		}
	}

	/**
	 * @return the locale passed in constructor.
	 */
	public Locale getLocale() {
		synchronized (streams) {
			if (locale == null) {
				locale = Locale.getDefault();
			}

			return locale;
		}
	}

	/**
	 * Changes the locale registered with this instance.
	 *
	 * @param locale locale
	 */
	public void setLocale(Locale locale) {
		if (locale == null) {
			throw new IllegalArgumentException("Null locale");
		}

		synchronized (streams) {
			this.locale = locale;
		}
	}

	/**
	 * @return Complete message to be printed.
	 * @param logType log type
	 * @param message message passed in
	 * {@linkplain Logger#writeLog(LogType, String)} call Default implementation
	 * just returns given message ignoring logType argument
	 */
	protected String getOutputMessage(LogType logType, String message) {
		return message;
	}

	/**
	 * Writes a message into all registered streams
	 *
	 * @param message message to be written
	 * @param logType log type
	 */
	public void writeLog(LogType logType, String message) {
		String tmpMessage = getOutputMessage(logType, message);
		synchronized (streams) {
			Set<LoggerStream> streamSet = streams.get(logType);
			if (streamSet != null) {
				for (LoggerStream stream : streamSet) {
					stream.println(logType, tmpMessage);
				}
			}
		}
	}
	// =========================================================================
}
