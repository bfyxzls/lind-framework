package com.lind.elasticsearch.util;

public class ParserUtils {

	/**
	 * 检验字符串是否为数组字符串
	 * @param str
	 * @return
	 */
	public static boolean isArray(String str) {
		String trim = str.trim();
		return trim.startsWith("[") && trim.endsWith("]");
	}

}
