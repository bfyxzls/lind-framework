package com.lind.common.util;

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

}
