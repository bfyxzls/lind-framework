package com.lind.common.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * 验证.
 */
public class ValidatorUtils {

	/**
	 * 正则表达式：验证手机号.
	 */
	private static final String REGEX_MOBILE = "^[1][0-9]{10}$";

	/**
	 * 正则表达式：验证邮箱.
	 */
	private static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

	/**
	 * 正则表达式：验证身份证.
	 */
	private static final String REGEX_ID_CARD = "([1-9]\\d{14})|([1-9]\\d{17})|([1-9]\\d{16}[x,X])";

	/**
	 * 校验手机号.
	 * @param mobile 手机号
	 * @return 校验通过返回true，否则返回false
	 */
	public static boolean isMobile(String mobile) {
		if (StringUtils.isEmpty(mobile)) {
			return false;
		}
		return Pattern.matches(REGEX_MOBILE, mobile);
	}

	/**
	 * 校验邮箱.
	 * @param email 邮箱
	 * @return 校验通过返回true，否则返回false
	 */
	public static boolean isEmail(String email) {
		return Pattern.matches(REGEX_EMAIL, email);
	}

	/**
	 * 校验身份证.
	 * @param idCard 身份证
	 * @return 校验通过返回true，否则返回false
	 */
	public static boolean isIdCard(String idCard) {
		if (Pattern.matches(REGEX_ID_CARD, idCard)) {
			String regionCode = idCard.substring(0, 2);
			if (validRegionCode(regionCode)) {
				// 身份证前两位地区编码正确
				if (idCard.length() == 18) {
					int year = Integer.parseInt(idCard.substring(6, 10));
					int month = Integer.parseInt(idCard.substring(10, 12));
					int day = Integer.parseInt(idCard.substring(12, 14));
					// 年月日合法，验证最后一位校验码
					if (validBirthDate(year, month, day)) {
						return validVarifyCode(idCard);
					}
				}
				else {
					int year = Integer.parseInt(idCard.substring(6, 8));
					int month = Integer.parseInt(idCard.substring(8, 10));
					int day = Integer.parseInt(idCard.substring(10, 12));
					return validBirthDate(year, month, day);
				}
			}
		}
		return false;
	}

	// 省份代码
	private static boolean validRegionCode(String regionCode) {
		String[] regionCodes = { "11", "12", "13", "14", "15", "21", "22", "23", "31", "32", "33", "34", "35", "36",
				"37", "41", "42", "43", "44", "45", "46", "50", "51", "52", "53", "54", "61", "62", "63", "64", "65",
				"71", "81", "82", "91" };
		return Arrays.binarySearch(regionCodes, regionCode) >= 0;
	}

	// 年月日判断
	private static boolean validBirthDate(int year, int month, int day) {
		// 判断年是几位数
		if (year <= 99) {
			year += 1900;
		}
		int[] bigMonths = { 1, 3, 5, 7, 8, 10, 12 };
		int[] smallMonths = { 4, 6, 9, 11 };

		// 大月（31天）
		if (Arrays.binarySearch(bigMonths, month) >= 0) {
			if (day <= 31 && day > 0) {
				return true;
			}
		}
		// 小月（30天）
		if (Arrays.binarySearch(smallMonths, month) >= 0) {
			if (day <= 30 && day > 0) {
				return true;
			}
		}
		// 2月
		if (month == 2) {
			// 判断是否为闰年
			if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
				return day <= 29 && day > 0;
			}
			else {
				return day <= 28 && day > 0;
			}
		}
		return false;
	}

	// 身份证最后一位检验码，计算公式：mod(Sum(ai * wi), 11)
	private static boolean validVarifyCode(String idCard) {
		// VarifyCode校验码、Wi身份证号码第i个位置上的加权因子
		String[] varifyCode = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };
		String[] wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2" };
		String ai = idCard.substring(0, 17);
		int sum = 0;
		for (int i = 0; i < 17; i++) {
			sum += Integer.parseInt(String.valueOf(ai.charAt(i))) * Integer.parseInt(wi[i]);
		}
		String strVerifyCode = varifyCode[sum % 11];
		return strVerifyCode.equals(idCard.substring(idCard.length() - 1));
	}

}
