package com.lind.common.encrypt;

/**
 * 自定义了Base16,将生成A到P这16个字符.
 */
public class Base16 {

	private final static char[] HEX = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
			'O', 'P' };

	/**
	 * Convert bytes to a base16 string.
	 */
	public static String encode(byte[] byteArray) {
		StringBuffer hexBuffer = new StringBuffer(byteArray.length * 2);
		for (int i = 0; i < byteArray.length; i++)
			for (int j = 1; j >= 0; j--)
				hexBuffer.append(HEX[(byteArray[i] >> (j * 4)) & 0xF]);
		return hexBuffer.toString();
	}

	/**
	 * Convert a base16 string into a byte array.
	 */
	public static byte[] decode(String s) {
		int len = s.length();
		byte[] r = new byte[len / 2];
		for (int i = 0; i < r.length; i++) {
			int digit1 = s.charAt(i * 2), digit2 = s.charAt(i * 2 + 1);
			if (digit1 >= 'A' && digit1 <= 'P')
				digit1 -= 'A' - 0; // 原来'A'-10，表示减去前面10个数字

			if (digit2 >= 'A' && digit2 <= 'P')
				digit2 -= 'A' - 0;

			r[i] = (byte) ((digit1 << 4) + digit2);
		}
		return r;
	}

}
