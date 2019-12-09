package org.slf4j.impl;

import org.slf4j.ILoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

import osgi.enroute.logger.simple.provider.LogForwarder;

/**
 * This class bridges between SLF4j by implementing a getSingleton() method on
 * the class with this name.
 */
public class StaticLoggerBinder implements LoggerFactoryBinder {

	/**
	 * Declare the version of the SLF4J API this implementation is compiled
	 * against. The value of this field is usually modified with each release.
	 */
	// To avoid constant folding by the compiler, this field must *not* be final
	public static String					REQUESTED_API_VERSION	= "1.7.26";	// !final
	private static final StaticLoggerBinder	SINGLETON;

	static {
		SINGLETON = new StaticLoggerBinder();
		REQUESTED_API_VERSION = "1.7.26";
	}

	public static final StaticLoggerBinder getSingleton() {
		return SINGLETON;
	}

	@Override
	public ILoggerFactory getLoggerFactory() {
		return LogForwarder.getLoggerFactory();
	}

	@Override
	public String getLoggerFactoryClassStr() {
		return LogForwarder.class.getName();
	}

}
