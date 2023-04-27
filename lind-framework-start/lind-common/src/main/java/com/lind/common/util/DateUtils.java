package com.lind.common.util;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

/**
 * 日期工具类.
 * DateTimeFormatter 是线程安全的
 */
public class DateUtils {
/**
 * Java8 新特性之新的时间和日期的使用
 Instant：时间戳
 Duration：持续时间，时间差
 LocalDate：只包含日期，比如：2021-09-02
 LocalTime：只包含时间，比如：23:12:10
 LocalDateTime：包含日期和时间，比如：2019-02-02 23:14:21
 Period：时间段
 ZoneOffset：时区偏移量，比如：+8:00
 ZonedDateTime：带时区的时间
 Clock：时钟，比如获取目前美国纽约的时间
 */
    /**
     * 默认日期格式化方式.
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final String DATE_FORMAT = "yyyy-MM-dd";


    private static final ThreadLocal<DateTimeFormatter> dateFormatterThreadLocal = ThreadLocal
            .withInitial(() -> DateTimeFormatter.ofPattern(DATE_FORMAT));

    private static final ThreadLocal<DateTimeFormatter> dateTimeFormatterThreadLocal = ThreadLocal
            .withInitial(() -> DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));

    private static DateTimeFormatter getDateTimeFormatter() {
        return dateTimeFormatterThreadLocal.get();
    }

    private static DateTimeFormatter getDateFormatter() {
        return dateFormatterThreadLocal.get();
    }


    /**
     * 将字符串类型日期转换日期时间.
     *
     * @param date 字符串日期
     */
    public static LocalDateTime getDateTimeFormat(String date) {
        if (StringUtils.isBlank(date)) {
            throw new IllegalArgumentException("日期不能为空");
        }
        try {
            DateTimeFormatter df = getDateTimeFormatter();
            LocalDateTime ldt = LocalDateTime.parse(date, df);
            return ldt;
        } catch (DateTimeParseException e) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
            LocalDateTime dt = LocalDateTime.parse(date);
            LocalDateTime ldt = LocalDateTime.parse(getDateFormat(dt), df);
            return ldt;
        } catch (Exception e) {
            throw new IllegalArgumentException("字符串日期转换LocalDateTime失败");
        }
    }

    /**
     * 将Date转换字符串类型日期.
     *
     * @param date 字符串日期
     */
    public static String getDateTimeFormat(LocalDateTime date) {
        if (date == null) {
            throw new IllegalArgumentException("日期不能为空");
        }
        try {
            String localTime = getDateTimeFormatter().format(date);
            return localTime;
        } catch (Exception e) {
            throw new IllegalArgumentException("LocalDateTime转换字符串类型日期失败");
        }
    }

    /**
     * 将Date转换字符串类型日期.
     *
     * @param date 字符串日期
     */
    public static String getDateFormat(LocalDateTime date) {
        if (date == null) {
            throw new IllegalArgumentException("日期不能为空");
        }
        try {
            String localTime = getDateFormatter().format(date);
            return localTime;
        } catch (Exception e) {
            throw new IllegalArgumentException("LocalDateTime转换字符串类型日期失败");
        }
    }

}
