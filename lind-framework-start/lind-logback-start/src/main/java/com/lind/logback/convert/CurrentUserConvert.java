package com.lind.logback.convert;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.slf4j.MDC;

import static com.lind.logback.mdc.LogInterceptor.CURRENT_USER;

/**
 * @author lind
 * @date 2023/1/28 10:49
 * @since 1.0.0
 */
public class CurrentUserConvert extends ClassicConverter {

	private static String EMPTY = "";

	@Override
	public String convert(ILoggingEvent event) {
		return MDC.get(CURRENT_USER) == null ? EMPTY : MDC.get(CURRENT_USER);
	}

}
