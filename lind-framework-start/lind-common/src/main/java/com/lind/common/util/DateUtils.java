package com.lind.common.util;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

/**
 * 日期工具类.
 */
public class DateUtils {

	/**
	 * 默认日期格式化方式.
	 */
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	private static final ThreadLocal<SimpleDateFormat> dateFormatThreadLocal = ThreadLocal
			.withInitial(() -> new SimpleDateFormat(DEFAULT_DATE_FORMAT));

	private static final ThreadLocal<DateTimeFormatter> dateTimeFormatterThreadLocal = ThreadLocal
			.withInitial(() -> DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));

	/**
	 * 线程安全的格式化.
	 * @param date
	 * @return
	 */
	public static String format(Date date) {
		return dateFormatThreadLocal.get().format(date);
	}

	private static DateTimeFormatter getDateTimeFormatter() {
		return dateTimeFormatterThreadLocal.get();
	}

	/**
	 * 将字符串类型日期转换Date.
	 * @param date 字符串日期
	 */
	public static LocalDateTime getDateFormat(String date) {
		if (StringUtils.isBlank(date)) {
			throw new IllegalArgumentException("日期不能为空");
		}
		try {
			DateTimeFormatter df = getDateTimeFormatter();
			LocalDateTime ldt = LocalDateTime.parse(date, df);
			return ldt;
		}
		catch (DateTimeParseException e) {
			DateTimeFormatter df = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
			LocalDateTime dt = LocalDateTime.parse(date);
			LocalDateTime ldt = LocalDateTime.parse(getDateFormat(dt), df);
			return ldt;
		}
		catch (Exception e) {
			throw new IllegalArgumentException("字符串日期转换LocalDateTime失败");
		}
	}

	/**
	 * 将Date转换字符串类型日期.
	 * @param date 字符串日期
	 */
	public static String getDateFormat(LocalDateTime date) {
		if (date == null) {
			throw new IllegalArgumentException("日期不能为空");
		}
		try {
			String localTime = getDateTimeFormatter().format(date);
			return localTime;
		}
		catch (Exception e) {
			throw new IllegalArgumentException("LocalDateTime转换字符串类型日期失败");
		}
	}

}
