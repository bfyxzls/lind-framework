package com.lind.common.othertest;

import cn.hutool.core.date.DateException;
import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.Test;
import org.springframework.util.SerializationUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@Slf4j
public class DateTimeTest {

	public static final String DATE_FORMAT_STR = "yyyy.MM.dd";

	public static String formatDate(String dateStr) {
		if (ObjectUtils.isEmpty(dateStr)) {
			return null;
		}
		try {
			return DateUtil.format(DateUtil.parse(dateStr), DATE_FORMAT_STR);
		}
		catch (DateException ex1) {
			ex1.getStackTrace();
			return null;
		}
	}

	/**
	 * 函数功能描述:UTC时间转本地时间格式
	 * @param utcTime UTC时间
	 * @param utcTimePatten UTC时间格式
	 * @param localTimePatten 本地时间格式
	 * @return 本地时间格式的时间 eg:utc2Local("2017-06-14T09:37:50.788+08:00",
	 * "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", "yyyy-MM-dd HH:mm:ss.SSS")
	 */
	public static String utc2Local(String utcTime, String utcTimePatten, String localTimePatten) {
		SimpleDateFormat utcFormater = new SimpleDateFormat(utcTimePatten);
		utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));// 时区定义并进行时间获取
		Date gpsUTCDate = null;
		try {
			gpsUTCDate = utcFormater.parse(utcTime);
		}
		catch (ParseException e) {
			e.printStackTrace();
			return utcTime;
		}
		SimpleDateFormat localFormater = new SimpleDateFormat(localTimePatten);
		localFormater.setTimeZone(TimeZone.getDefault());
		String localTime = localFormater.format(gpsUTCDate.getTime());
		return localTime;
	}

	@Test
	public void now() {
		int offset = 1;
		int currentTime = ((int) (System.currentTimeMillis() / 1000)) + offset;
		log.info("currentTime={}", currentTime);
	}

	@Test
	public void dateStringFormat() {
		String utcTime = "2018-05-23T16:05:52.123+00:00";
		String utcTimePatten = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
		String localTimePatten = "yyyy-MM-dd HH:mm:ss.SSS";
		System.out.println(utcTime);
		System.out.println(utc2Local(utcTime, utcTimePatten, localTimePatten));
	}

	@Test
	public void convertTest() {
		Map<String, byte[]> dataRecord = new HashMap<>();
		dataRecord.put("time", SerializationUtils.serialize("2022.11.01"));
		byte[] result = SerializationUtils.serialize(dataRecord);
		Map<String, byte[]> mapResult = (Map<String, byte[]>) SerializationUtils.deserialize(result);
		Object date = SerializationUtils.deserialize(mapResult.get("time"));
		System.out.println(date);

	}

	@Test
	public void convert() throws JsonProcessingException {

		String jsonStr = "{\"createTime\":\"2021-09-29 08:17:24.123\"}";
		String jsonStr3 = "{\"createTime\":\"2021-09-29 08:17:24\"}";
		String jsonStr2 = "{\"createTime\":\"2021-09-29'T'08:17:24.123\"}";

		Json json = new ObjectMapper().readValue(jsonStr, Json.class);
		Json json3 = new ObjectMapper().readValue(jsonStr3, Json.class);
		Json json2 = new ObjectMapper().readValue(jsonStr2, Json.class);

		System.out.println(json.getCreateTime());
		System.out.println(json2.getCreateTime());
		System.out.println(json3.getCreateTime());

	}

	@Test
	public void dateTest() {
		System.out.println(formatDate("1998-09-01"));
	}

	@Test
	public void cacleBetweenDaysJava8() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date d1 = new Date();
		Date d2 = sdf.parse("2023-07-02 00:00:10");
		long daysBetween = (d2.getTime() - d1.getTime() + 1000000) / (60 * 60 * 24 * 1000);
		System.out.println("相隔 " + daysBetween + " 天");
	}

	@Data
	static class Json {

		@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 精确到秒
														// @JsonFormat(pattern="yyyy-MM-dd")
														// //精确到天
		private Date createTime;

	}

}
