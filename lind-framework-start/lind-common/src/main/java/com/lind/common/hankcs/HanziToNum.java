package com.lind.common.hankcs;

/**
 * @author lind
 * @date 2023/2/27 16:37
 * @since 1.0.0
 */
public class HanziToNum {

	public static String num2chinese(String string) {
		String[] s1 = { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
		String[] s2 = { "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千" };

		String result = "";

		int n = string.length();
		for (int i = 0; i < n; i++) {

			int num = string.charAt(i) - '0';

			if (i != n - 1 && num != 0) {
				result += s1[num] + s2[n - 2 - i];
			}
			else {
				result += s1[num];
			}
		}

		if (result.startsWith("一十")) {
			result = result.substring(1);
		}

		result = result.replace("零零零", "零");
		result = result.replace("零零", "零");

		if (result.endsWith("零")) {
			result = result.substring(0, result.length() - 1);
		}

		return result;

	}

	public static int chinese2num(String chinese) {
		int result = 0;
		int temp = 1;// 存放一个单位的数字如：十万
		int count = 0;// 判断是否有chArr
		char[] cnArr = new char[] { '一', '二', '三', '四', '五', '六', '七', '八', '九' };
		char[] chArr = new char[] { '十', '百', '千', '万', '亿' };
		for (int i = 0; i < chinese.length(); i++) {
			boolean b = true;// 判断是否是chArr
			char c = chinese.charAt(i);
			for (int j = 0; j < cnArr.length; j++) {// 非单位，即数字
				if (c == cnArr[j]) {
					if (0 != count) {// 添加下一个单位之前，先把上一个单位值添加到结果中
						result += temp;
						temp = 1;
						count = 0;
					}
					// 下标+1，就是对应的值
					temp = j + 1;
					b = false;
					break;
				}
			}
			if (b) {// 单位{'十','百','千','万','亿'}
				for (int j = 0; j < chArr.length; j++) {
					if (c == chArr[j]) {
						switch (j) {
							case 0:
								temp *= 10;
								break;
							case 1:
								temp *= 100;
								break;
							case 2:
								temp *= 1000;
								break;
							case 3:
								temp *= 10000;
								break;
							case 4:
								temp *= 100000000;
								break;
							default:
								break;
						}
						count++;
					}
				}
			}
			if (i == chinese.length() - 1) {// 遍历到最后一个字符
				result += temp;
			}
		}
		return result;
	}

	public static void main(String[] args) {
		String s = num2chinese("2002");
		long num = chinese2num("二百零三");
		System.out.println(s);
		System.out.println(num);
	}

}
