package com.lind.logback;

import ch.qos.logback.classic.PatternLayout;
import com.lind.logback.convert.BrowserTypeConvert;
import com.lind.logback.convert.CurrentUserConvert;
import com.lind.logback.convert.IpConvert;
import com.lind.logback.convert.LevelColorConverter;
import com.lind.logback.convert.OsConvert;
import com.lind.logback.convert.TraceIdConvert;
import com.lind.logback.convert.UriConvert;

/**
 * 将扩展的classicConvert进行注册，以后不需要在logback-spring.xml加添加conversionRule配置项了，直接修改layout项即可
 *
 * @author lind
 * @date 2023/1/28 14:31
 * @since 1.0.0
 */
public class StandardPatternLayout extends PatternLayout {

	static {
		PatternLayout.defaultConverterMap.put("ip", IpConvert.class.getName());
		PatternLayout.defaultConverterMap.put("color", LevelColorConverter.class.getName());
		PatternLayout.defaultConverterMap.put("currentUser", CurrentUserConvert.class.getName());
		PatternLayout.defaultConverterMap.put("os", OsConvert.class.getName());
		PatternLayout.defaultConverterMap.put("browserType", BrowserTypeConvert.class.getName());
		PatternLayout.defaultConverterMap.put("uri", UriConvert.class.getName());
		PatternLayout.defaultConverterMap.put("traceId", TraceIdConvert.class.getName());

	}

}
