package com.lind.common.core.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class ConvertUtils {

	/**
	 * 将16进制的字符串转化为long值.
	 */
	public static Long parseString16ToLong(String str16) {
		if (str16 == null) {
			throw new NumberFormatException("null");
		}
		// 先转化为小写
		str16 = str16.toLowerCase();
		// 如果字符串以0x开头，去掉0x
		str16 = str16.startsWith("0x") ? str16.substring(2) : str16;
		if (str16.length() > 16) {
			throw new NumberFormatException("For input string '" + str16 + "' is to long");
		}
		return Long.parseLong(str16, 16);
	}

	/**
	 * 将整型转长整型.
	 */
	public static List<Long> convertLong(List<Integer> list) {
		List<Long> result = new ArrayList<>();
		for (Integer item : list) {
			result.add(Long.valueOf(item));
		}
		return result;
	}

	/**
	 * 将字符转长整型.
	 */
	public static List<Long> convertLongFromString(List<String> list) {
		List<Long> result = new ArrayList<>();
		for (String item : list) {
			result.add(Long.parseLong(item));
		}
		return result;
	}

	/**
	 * 时间戳转日期格式.
	 */
	public static String timeStamp2Date(Long seconds, String format) {
		if (seconds == null || seconds.equals(0L)) {
			return "";
		}

		if (StringUtils.isEmpty(format)) {
			format = "yyyy-MM-dd'T'HH:mm:ss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date(seconds));
	}

	/**
	 * 四舍五入.
	 */
	public static String round(double value, int digit) {
		NumberFormat nf = NumberFormat.getNumberInstance();
		// 保留两位小数
		nf.setMaximumFractionDigits(digit);
		nf.setRoundingMode(RoundingMode.DOWN);
		return nf.format(value);

	}

	/**
	 * 字符数组转Long数组.
	 */
	public static Long[] toLongArray(String[] param) {
		Long[] num = new Long[param.length];
		for (int idx = 0; idx < param.length; idx++) {
			num[idx] = Long.parseLong(param[idx]);
		}
		return num;
	}

}
