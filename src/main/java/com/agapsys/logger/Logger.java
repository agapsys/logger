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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Logger for multiple stream outputs.
 * This class is intended to be thread safe
 * @author Leandro Oliveira (leandro@agapsys.com)
 */
public class Logger {
	private final List<LoggerStream> streams = new LinkedList<>();
	private final Object synchronizer = new Object();
	private final DateFormat dateFormatter = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss] ");
	
	private Locale locale;
	
	
	/** Constructor. */
	public Logger() {
		this(Locale.getDefault());
	}
	
	/**
	 * Constructor.
	 * @param locale locale used in this instance.
	 */
	public Logger(Locale locale) {
		if (locale == null)
			throw new IllegalArgumentException("Null locale");
		
		this.locale = locale;
	}

	/**
	 * Adds an output stream to this logger instance.
	 * @param stream stream to be associated with this logger instance.
	 */
	public void addStream(LoggerStream stream) {
		synchronized(synchronizer) {
			streams.add(stream);
		}
	}
	
	/** 
	 * Removes an stream.
	 * @param stream stream to be removed.
	 */
	public void removeStream(LoggerStream stream) {
		synchronized(synchronizer) {
			streams.remove(stream);
		}
	}

	/** Remove all registered streams. */
	public void removeAllStreams() {
		synchronized (synchronizer) {
			streams.clear();
		}
	}
	
	/** @return the locale passed in constructor. */
	public Locale getLocale() {
		if (locale == null)
			locale = Locale.getDefault();
		
		return locale;
	}
	
	/** 
	 * Changes the locale registered with this instance.
	 * @param locale locale
	 */
	public void setLocale(Locale locale) {
		if (locale == null)
			throw new IllegalArgumentException("Null locale");
		
		synchronized(synchronizer) {
			this.locale = locale;
		}
	}
	
	/** 
	 * @param date date to be formatted
	 * @return given date formatted as string.
	 */
	protected String getFormattedDate(Date date) {
		return dateFormatter.format(date);
	}
	
	/** @returns Complete message to be printed. */
	private String getOutputMessage(String message, boolean includeTimeStamp) {
		String timeString;
		
		if (includeTimeStamp) {
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			
			timeString = getFormattedDate(now);
		} else {
			timeString = "";
		}
		
		
		return timeString + message;
	}
	
	/**
	 * Writes a message into all registered streams
	 * @param message message to be written
	 * @param includeTimeStamp defines if a timestamp shall be included in message.
	 */
	public void writeLog(String message, boolean includeTimeStamp) {
		String tmpMessage = getOutputMessage(message, includeTimeStamp);
		synchronized(synchronizer) {
			for (LoggerStream stream : streams) {
				stream.println(tmpMessage);
			}
		}
	}

	/** 
	 * Writes a message into all registered streams.
	 * Convenience method for writeLog(message, true).
	 * @param message message to be written
	 * @see Logger#writeLog(String, boolean)
	 */
	public void writLog(String message) {
		writeLog(message, true);
	}
	// =========================================================================
}
