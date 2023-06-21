package com.lind.common.core.util;

/**
 * 二进制与十六进制转换.
 */
public class BinHexSwitchUtil {

	/**
	 * 16进制转2进制向右补零
	 * @param hex
	 * @param length
	 * @return
	 */
	public static String hexStringToBinRightZero(String hex, Integer length) {
		int i = Integer.parseInt(hex, 16);
		String str2 = Integer.toBinaryString(i);
		String result = str2 + String.format("%1$0" + (length - str2.length()) + "d", 0);
		return result;
	}

	/**
	 * 16进制转2进制向左补零
	 * @param hex
	 * @param length
	 * @return
	 */
	public static String hexStringToBinLeftZero(String hex, Integer length) {
		int i = Integer.parseInt(hex, 16);
		String str2 = Integer.toBinaryString(i);
		String result = String.format("%0" + length + "d", Integer.valueOf(str2));
		return result;
	}

	/**
	 * 16进制转2进制
	 * @param hex
	 * @return
	 */
	public static String hexStringToBin(String hex) {
		int i = Integer.parseInt(hex, 16);
		String str2 = Long.toBinaryString(i);
		return str2;
	}

	/**
	 * 二进制反转.
	 * @param bin
	 * @return
	 */
	public static String binReverse(String bin) {
		return new StringBuffer(bin).reverse().toString();
	}

	/**
	 * 2进制转16进制
	 * @param bin
	 * @return
	 */
	public static String binStringToHex(String bin) {
		return Long.toHexString(Long.parseLong(bin, 2));
	}

	/**
	 * byte[]进制转16进制字符串
	 * @param bytes
	 * @return
	 */
	public static String bytesToHexString(byte[] bytes) {
		/*
		 * 1. 定义一个StringBuilder对象sb，用于拼接转换后的16进制字符串。 2. 遍历byte[]数组中的每一个字节，将每个字节转换成16进制数。
		 * 3. 使用Integer.toHexString(b & 0xFF)方法将一个字节转换成16进制数。其中，b &
		 * 0xFF将字节的高位清零，以避免转换出现错误。 4. 如果一个字节转换成的16进制数长度为1，则在它的前面添加一个'0'。 5.
		 * 把16进制数添加到StringBuilder对象sb中。 6. 返回StringBuilder对象sb转换成的String类型的字符串。
		 */
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			String hex = Integer.toHexString(b & 0xFF);
			if (hex.length() == 1) {
				sb.append("0");
			}
			sb.append(hex);
		}
		return sb.toString();
	}

	/**
	 * 十六进制转byte[]
	 * @param hexString
	 * @return
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString.length() % 2 != 0) {
			throw new IllegalArgumentException("Invalid hex string length: " + hexString.length());
		}
		int len = hexString.length() / 2;
		byte[] bytes = new byte[len];
		for (int i = 0; i < len; i++) {
			int hi = Character.digit(hexString.charAt(i * 2), 16);
			int lo = Character.digit(hexString.charAt(i * 2 + 1), 16);
			if (hi == -1 || lo == -1) {
				throw new IllegalArgumentException("Invalid hex string: " + hexString);
			}
			bytes[i] = (byte) (hi * 16 + lo);
		}
		return bytes;
	}

}
