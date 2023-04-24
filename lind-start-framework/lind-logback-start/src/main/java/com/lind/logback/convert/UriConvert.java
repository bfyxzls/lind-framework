package com.lind.logback.convert;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.slf4j.MDC;

import static com.lind.logback.mdc.LogInterceptor.URI;

/**
 * @author lind
 * @date 2023/1/28 13:39
 * @since 1.0.0
 */
public class UriConvert extends ClassicConverter {

	@Override
	public String convert(ILoggingEvent event) {
		return MDC.get(URI) == null ? "" : MDC.get(URI);
	}

}
