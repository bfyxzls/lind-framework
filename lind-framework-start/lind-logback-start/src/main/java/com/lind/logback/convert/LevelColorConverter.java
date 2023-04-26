package com.lind.logback.convert;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * 日志模板：基于日志级别的颜色的转换器
 */
public class LevelColorConverter extends ClassicConverter {

	/**
	 *
	 * 'blue' : ['\x1B[34m', '\x1B[39m'], 'cyan' : ['\x1B[36m', '\x1B[39m'], 'green' :
	 * ['\x1B[32m', '\x1B[39m'], 'magenta' : ['\x1B[35m', '\x1B[39m'], 'red' :['\x1B[31m',
	 * '\x1B[39m'], 'yellow' : ['\x1B[33m', '\x1B[39m'],
	 **/
	private static final String END_COLOR = "\u001b[m";

	private static final String DEBUG_COLOR = "\u001b[0;34m";// blue

	private static final String INFO_COLOR = "\u001b[0;32m";// green

	private static final String WARN_COLOR = "\u001b[0;33m";// yellow

	private static final String ERROR_COLOR = "\u001b[0;31m";// red,8进制的31

	@Override
	public String convert(ILoggingEvent event) {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(getColor(event.getLevel()));
		String result = String.format("%5s", event.getLevel());
		sbuf.append(result);
		sbuf.append(END_COLOR);
		return sbuf.toString();
	}

	/**
	 * Returns the appropriate characters to change the color for the specified logging
	 * level.
	 */
	private String getColor(Level level) {
		switch (level.toInt()) {
		case Level.DEBUG_INT:
			return DEBUG_COLOR;
		case Level.INFO_INT:
			return INFO_COLOR;
		case Level.ERROR_INT:
			return ERROR_COLOR;
		case Level.WARN_INT:
			return WARN_COLOR;
		default:
			return "";
		}
	}

}
