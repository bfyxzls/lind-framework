package com.lind.logback.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

/**
 * 自定义一个logger Appender,用来存储日志信息.
 *
 * @author lind
 * @date 2023/1/29 17:36
 * @since 1.0.0
 */
public class MapAppender extends AppenderBase<ILoggingEvent> {

	MapHolder holder = MapHolder.create();

	@Override
	protected void append(ILoggingEvent event) {
		holder.putEvent(String.valueOf(System.nanoTime()), event.getMessage());
	}

}
