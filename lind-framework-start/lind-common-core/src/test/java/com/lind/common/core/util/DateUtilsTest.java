package com.lind.common.core.util;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author lind
 * @date 2023/7/13 14:35
 * @since 1.0.0
 */
public class DateUtilsTest {

	@Test
	public void getDate() {
		System.out.println(DateUtils.getDateTimeFormat("2022-02-29 12:30:30")); // 转自动转当前月最大值，为2022-02-28
																				// 12:30:30
	}

	@Test
	public void format() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHH");
		String formattedDateTime = now.format(formatter);
		System.out.println(formattedDateTime);
	}

}
