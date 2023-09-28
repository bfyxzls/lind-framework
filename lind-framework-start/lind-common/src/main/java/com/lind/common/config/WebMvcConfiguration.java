package com.lind.common.config;

import cn.hutool.core.date.DatePattern;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * WebMvc配置.
 *
 * @author lind
 * @date 2023/5/24 14:46
 * @since 1.0.0
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

	/**
	 * 增加GET请求参数中时间类型转换，注意是LocalTime,LocalDate和LocalDateTime,因为你配置的是DateTimeFormatter.
	 * <ul>
	 * <li>HH:mm:ss -> LocalTime</li>
	 * <li>yyyy-MM-dd -> LocalDate</li>
	 * <li>yyyy-MM-dd HH:mm:ss -> LocalDateTime</li>
	 * </ul>
	 * @param registry
	 */
	@Override
	public void addFormatters(FormatterRegistry registry) {
		DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
		// LocalDate,LocalTime,LocalDateTime格式化
		registrar.setTimeFormatter(DatePattern.NORM_TIME_FORMATTER);
		registrar.setDateFormatter(DatePattern.NORM_DATE_FORMATTER);
		registrar.setDateTimeFormatter(DatePattern.NORM_DATETIME_FORMATTER);
		registrar.registerFormatters(registry);
		// java.util.Date日期格式化
		registry.addFormatter(new CompositeFormatter());
	}

	/**
	 * java.util.Date日期格式化.
	 */
	private static class CompositeFormatter implements Formatter<Date> {

		private final List<Formatter<Date>> formatters = Arrays.asList(new DateFormatter("yyyy-MM-dd HH:mm:ss"),
				new DateFormatter("yyyy-MM-dd"), new DateFormatter("HH:mm:ss"));

		@Override
		public Date parse(String text, Locale locale) throws ParseException {
			for (Formatter<Date> formatter : formatters) {
				try {
					return formatter.parse(text, locale);
				}
				catch (ParseException ignored) {
				}
			}
			throw new ParseException("Unable to parse date: " + text, 0);
		}

		@Override
		public String print(Date date, Locale locale) {
			return formatters.get(0).print(date, locale);
		}

	}

}
