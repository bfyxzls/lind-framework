package com.lind.logback.convert;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author lind
 * @date 2023/1/28 10:47
 * @since 1.0.0
 */
public class IpConvert extends ClassicConverter {

	private static String webIP;
	static {
		try {
			webIP = InetAddress.getLocalHost().getHostAddress();
		}
		catch (UnknownHostException e) {
			webIP = null;
		}
	}

	/**
	 * logback中获取相关信息
	 * @param event
	 * @return
	 */
	@Override
	public String convert(ILoggingEvent event) {
		return webIP;
	}

}
