package com.lind.common.util;

/**
 * 二进制与十六进制转换.
 */
public class BinHexSwitchUtils {

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
	 * byte[]转十六进制
	 * @param bytes
	 * @return
	 */
	public static String bytesToHex(byte[] bytes) {
		StringBuilder hex = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			byte b = bytes[i];
			boolean flag = false;
			if (b < 0)
				flag = true;
			int absB = Math.abs(b);
			if (flag)
				absB = absB | 0x80;
			String tmp = Integer.toHexString(absB & 0xFF);
			if (tmp.length() == 1) { // 转化的十六进制不足两位，需要补0
				hex.append("0");
			}
			hex.append(tmp.toLowerCase());
		}
		return hex.toString();
	}

	/**
	 * 十六进制转byte[]
	 * @param hex
	 * @return
	 */
	public static byte[] hexToBytes(String hex) {
		byte[] bytes = new byte[hex.length() / 2];
		for (int i = 0; i < hex.length(); i = i + 2) {
			String subStr = hex.substring(i, i + 2);
			boolean flag = false;
			int intH = Integer.parseInt(subStr, 16);
			if (intH > 127)
				flag = true;
			if (intH == 128) {
				intH = -128;
			}
			else if (flag) {
				intH = 0 - (intH & 0x7F);
			}
			byte b = (byte) intH;
			bytes[i / 2] = b;
		}
		return bytes;
	}

}
