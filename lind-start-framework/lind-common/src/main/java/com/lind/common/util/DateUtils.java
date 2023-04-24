package com.lind.common.util;

import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类.
 */
public class DateUtils {

	/**
	 * 默认日期格式化方式.
	 */
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 将字符串类型日期转换Date.
	 * @param date 字符串日期
	 */
	public static LocalDateTime getDateFormat(String date) {
		if (StringUtils.isBlank(date)) {
			throw new IllegalArgumentException("日期不能为空");
		}
		try {
			DateTimeFormatter df = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
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
			throw new IllegalArgumentException("字符串日期转换date失败");
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
			DateTimeFormatter df = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
			String localTime = df.format(date);
			return localTime;
		}
		catch (Exception e) {
			throw new IllegalArgumentException("LocalDateTime转换字符串类型日期失败");
		}
	}

	/**
	 * 时分秒.
	 * @param time .
	 */
	public static String secToTime(int time) {
		String timeStr = null;
		int hour = 0;
		int minute = 0;
		int second = 0;
		if (time <= 0) {
			return "00:00";
		}
		else {
			minute = time / 60;
			if (minute < 60) {
				second = time % 60;
				timeStr = unitFormat(minute) + ":" + unitFormat(second);
			}
			else {
				hour = minute / 60;
				if (hour > 99) {
					return "99:59:59";
				}
				minute = minute % 60;
				second = time - hour * 3600 - minute * 60;
				timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
			}
		}
		return timeStr;
	}

	/**
	 * unitFormat.
	 */
	public static String unitFormat(int i) {
		String retStr = null;
		if (i >= 0 && i < 10) {
			retStr = "0" + Integer.toString(i);
		}
		else {
			retStr = "" + i;
		}
		return retStr;

	}

	/**
	 * LocalDateTime 转换 localDate.
	 */
	public static LocalDate toLocalDate(LocalDateTime localDateTime) {
		if (localDateTime != null) {
			return localDateTime.toLocalDate();
		}
		return null;
	}

	/**
	 * LocalDateTime 转换 Date.
	 */
	public static Date toDate(LocalDateTime localDateTime) {
		ZoneId zone = ZoneId.systemDefault();
		Instant instant = localDateTime.atZone(zone).toInstant();
		return Date.from(instant);
	}

	/**
	 * 是否相同周.
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameWeek(Date date1, Date date2) {
		boolean result = false;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date1);
		int weekNum1 = calendar.get(Calendar.WEEK_OF_YEAR);

		calendar.setTime(date2);
		int weekNum2 = calendar.get(Calendar.WEEK_OF_YEAR);
		if (weekNum1 == weekNum2) {
			result = true;
		}
		return result;
	}

}
