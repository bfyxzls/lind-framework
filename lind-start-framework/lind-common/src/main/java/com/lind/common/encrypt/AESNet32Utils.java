package com.lind.common.encrypt;

import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.UUID;

/**
 * .net的aes加密32个字符
 */
public class AESNet32Utils {

	/**
	 * 获取32位的key.
	 * @return
	 */
	public static String GetAesKey() {
		String key = UUID.randomUUID().toString().replaceAll("-", "");
		return key;
	}

	/**
	 * 加密.
	 * @param content
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String content, String key) throws Exception {
		String IV = key;
		if (key.length() > 16) {
			// IV为商户MD5密钥后16位
			IV = key.substring(key.length() - 16);
			// RES的KEY 为商户MD5密钥的前16位
			key = key.substring(0, 16);
		}

		return encryptData(content, key, IV);
	}

	/**
	 * 解密.
	 * @param content
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String content, String key) throws Exception {
		String IV = key;
		if (key.length() > 16) {
			// IV为商户MD5密钥后16位
			IV = key.substring(key.length() - 16);
			// RES的KEY 为商户MD5密钥的前16位
			key = key.substring(0, 16);
		}
		return decryptData(content, key, IV);
	}

	/**
	 * aes 加密
	 * @param data
	 * @return
	 */
	public static String encryptData(String data, String key, String IV) throws Exception {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			byte[] dataBytes = data.getBytes("UTF-8");
			int plaintextLength = dataBytes.length;
			byte[] plaintext = new byte[plaintextLength];
			System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
			SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
			IvParameterSpec ivspec = new IvParameterSpec(IV.getBytes());
			cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
			byte[] encrypted = cipher.doFinal(plaintext);
			String result = bytesToHexString(encrypted);
			return result;
		}
		catch (Exception e) {
			throw e;
		}

	}

	/**
	 * aes 解密
	 * @param data 密文
	 * @return
	 */
	public static String decryptData(String data, String key, String IV) throws Exception {
		try {
			byte[] encrypted1 = hexStringToBytes(data);
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
			IvParameterSpec ivspec = new IvParameterSpec(IV.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
			byte[] original = cipher.doFinal(encrypted1);
			String originalString = new String(original, "UTF-8");
			return originalString;
		}
		catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 数组转十六进制数.
	 * @param src
	 * @return
	 */
	private static String bytesToHexString(byte[] src) {
		StringBuilder builder = new StringBuilder();
		if (src == null || src.length <= 0) {
			return null;
		}
		String hv;
		for (int i = 0; i < src.length; i++) {
			hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
			if (hv.length() < 2) {
				builder.append(0);
			}
			builder.append(hv);
		}
		return builder.toString();
	}

	/**
	 * 将Hex String转换为Byte数组
	 * @param hexString the hex string
	 * @return the byte [ ]
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (StringUtils.isEmpty(hexString)) {
			return null;
		}
		hexString = hexString.toLowerCase();
		final byte[] byteArray = new byte[hexString.length() >> 1];
		int index = 0;
		for (int i = 0; i < hexString.length(); i++) {
			if (index > hexString.length() - 1) {
				return byteArray;
			}
			byte highDit = (byte) (Character.digit(hexString.charAt(index), 16) & 0xFF);
			byte lowDit = (byte) (Character.digit(hexString.charAt(index + 1), 16) & 0xFF);
			byteArray[i] = (byte) (highDit << 4 | lowDit);
			index += 2;
		}
		return byteArray;
	}

}
