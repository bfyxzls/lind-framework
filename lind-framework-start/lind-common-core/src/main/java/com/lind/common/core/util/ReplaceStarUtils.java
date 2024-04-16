package com.lind.common.core.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 星号类.
 */
public class ReplaceStarUtils {

	private ReplaceStarUtils() {
		throw new AssertionError();
	}

	/**
	 * 实际替换动作.
	 * @param param .
	 * @return .
	 */
	public static String replaceAction(String param) {
		String userNameAfterReplaced = "";
		int nameLength = param.length();
		if (nameLength < 3 && nameLength > 0) {
			if (nameLength == 1) {
				userNameAfterReplaced = "*";
			}
			else {
				userNameAfterReplaced = param.replaceAll(param, "^.{1,2}");
			}
		}
		else {
			int num1;
			int num2;
			int num3;
			num2 = (int) Math.ceil(nameLength / 3.0);
			num1 = (int) Math.floor(nameLength / 3.0);
			num3 = nameLength - num1 - num2;
			String star = StringUtils.repeat("*", num2);
			userNameAfterReplaced = param.replaceAll("(.{" + num1 + "})(.{" + num2 + "})(.{" + num3 + "})",
					"$1" + star + "$3");
		}
		return userNameAfterReplaced;
	}

	/**
	 * 加星号返回.
	 */
	public static String maskSensitiveInfo(String input) {
		// 匹配手机号码和邮箱地址的正则表达式
		String phonePattern = "(\\d{3})\\d{4}(\\d{4})";
		String emailPattern = "(.{1,4})(.*)(@.*)";

		if (input.matches(phonePattern)) {
			return input.replaceAll(phonePattern, "$1****$2"); // 对手机号中间四位加*
		} else if (input.matches(emailPattern)) {
			return input.replaceAll(emailPattern, "$1****$3"); // 对邮箱账号中间部分加*
		} else {
			return input; // 如果不是手机号或邮箱，则直接返回原始输入
		}
	}
}
