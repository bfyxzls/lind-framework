package com.lind.common.core.util;

import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 相似度.
 */
public class SimilarUtil {

	// [^>]*?匹配除了字符>
	// *?
	private static final Pattern pattern = Pattern.compile("(<a href=[^>]*? class=.fjLink[^>]*?>).*?</a>");

	public static LinkedList<String> tagA(String input) {

		LinkedList<String> tagAList = new LinkedList<>();

		Matcher matcher = pattern.matcher(input);
		while (matcher.find()) {
			tagAList.add(matcher.start() + 1 + "," + (matcher.end() - 4));
		}

		return tagAList;
	}

	/**
	 * 比较两个字符的相似度，为1表示相等.
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static double degree(String str1, String str2) {
		/*
		 * 先移除无意义字符，再比较长短，否则 如果长串中无效字符过多会出现 ArrayIndexOutOfBoundsException
		 */
		String newStrA = removeSign(str1);
		String newStrB = removeSign(str2);

		if (newStrA.length() < newStrB.length()) {
			String temps = newStrA;
			newStrA = newStrB;
			newStrB = temps;
		}

		return longestCommonSubstring(newStrA, newStrB);

		// 用较大的字符串长度作为分母，相似子串作为分子计算出字串相似度
		/*
		 * int temp = Math.max(newStrA.length(), newStrB.length()); int temp2 =
		 * longestCommonSubstring(newStrA, newStrB).length(); return temp2 * 1.0 / temp;
		 */
	}

	/**
	 * 将字符串的所有数据依次写成一行
	 */
	private static String removeSign(String str) {
		StringBuilder sb = new StringBuilder();
		// 遍历字符串str,如果是汉字数字或字母，则追加到ab上面
		for (char item : str.toCharArray()) {
			if (charReg(item)) {
				sb.append(item);
			}
		}
		return sb.toString();
	}

	/**
	 * 判断字符是否为汉字，数字和字母， 因为对符号进行相似度比较没有实际意义，故符号不加入考虑范围。
	 */
	private static boolean charReg(char charValue) {
		return (charValue >= 0x4E00 && charValue <= 0X9FA5) || (charValue >= 'a' && charValue <= 'z')
				|| (charValue >= 'A' && charValue <= 'Z') || (charValue >= '0' && charValue <= '9');
	}

	/**
	 * 求公共子串，采用动态规划算法。 其不要求所求得的字符在所给的字符串中是连续的。
	 */
	private static double longestCommonSubstring(String strA, String strB) {
		char[] chars_strA = strA.toCharArray();
		char[] chars_strB = strB.toCharArray();
		int m = chars_strA.length;
		int n = chars_strB.length;

		/*
		 * 初始化矩阵数据,matrix[0][0]的值为0，
		 * 如果字符数组chars_strA和chars_strB的对应位相同，则matrix[i][j]的值为左上角的值加1，
		 * 否则，matrix[i][j]的值等于左上方最近两个位置的较大值， 矩阵中其余各点的值为0.
		 */
		int[][] matrix = new int[m + 1][n + 1];
		for (int i = 1; i <= m; i++) {
			for (int j = 1; j <= n; j++) {
				if (chars_strA[i - 1] == chars_strB[j - 1]) {
					matrix[i][j] = matrix[i - 1][j - 1] + 1;

				}
				else {
					matrix[i][j] = Math.max(matrix[i][j - 1], matrix[i - 1][j]);

				}
			}
		}

		return matrix[m][n] * 1.0 / strA.length();

		/*
		 * 此时 matrix[m][n] 的值就是相似字符串的长度，如果不需要返回结果，下面可以忽略
		 */

		/*
		 * 矩阵中，如果matrix[m][n]的值不等于matrix[m-1][n]的值也不等于matrix[m][n-1]的值，
		 * 则matrix[m][n]对应的字符为相似字符元，并将其存入result数组中。
		 */
		/*
		 * char[] result = new char[matrix[m][n]]; int currentIndex = result.length - 1;
		 * while (matrix[m][n] != 0) { if (matrix[n] == matrix[n - 1]) { n--;
		 *
		 * } else if (matrix[m][n] == matrix[m - 1][n]) { m--;
		 *
		 * } else { result[currentIndex] = chars_strA[m - 1]; currentIndex--; n--; m--; }
		 * } return new String(result);
		 */
	}

	/*
	 * 结果转换成百分比形式
	 */
	public static String similarityResult(double resule) {
		return NumberFormat.getPercentInstance(new Locale("en ", "US ")).format(resule);
	}

}
