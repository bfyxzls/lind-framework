package com.lind.common.encrypt;

import com.lind.common.encrypt.hash.Base16;
import com.lind.common.encrypt.hash.Base62;
import lombok.SneakyThrows;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * 散列算法-加密工具类.
 */
public class HashUtils {

	private static final char[] encodes = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
			.toCharArray();

	private static final byte[] decodes = new byte[256];

	static {
		for (int i = 0; i < encodes.length; i++) {
			decodes[encodes[i]] = (byte) i;
		}
	}

	private HashUtils() {
		throw new AssertionError();
	}

	/**
	 * BASE64解密.
	 * @param key .
	 * @return .
	 */
	public static byte[] decryptBASE64(String key) {
		return (Base64.getDecoder().decode(key));
	}

	/**
	 * BASE64加密.
	 * @param key .
	 * @return .
	 */
	public static String encryptBASE64(byte[] key) {
		return (Base64.getEncoder().encodeToString(key));
	}

	/**
	 * base62编码.
	 * @param data
	 * @return
	 */
	public static String encryptBASE62(byte[] data) {
		return new String(Base62.createInstance().encode(data));
	}

	/**
	 * base62解码.
	 */
	public static byte[] decryptBASE62(String str) {
		return Base62.createInstance().decode(str.getBytes());
	}

	/**
	 * 自定义base162编码【A-P】.
	 * @param data
	 * @return
	 */
	public static String encryptBASE16(byte[] data) {
		return Base16.encode(data);
	}

	/**
	 * 自定义base162解码【A-P】.
	 */
	public static byte[] decryptBASE16(String str) {
		return Base16.decode(str);
	}

	/**
	 * 长度为128位，32个16进制数组成的MD5散列加密算法.
	 * @param inputStr 明文
	 * @return .
	 */
	@SneakyThrows
	public static String md5(String inputStr) {
		return md5(inputStr, null);
	}

	/**
	 * 加盐md5，长度为128位，32个16进制数组成的MD5散列加密算法.
	 * @param passwordToHash
	 * @param salt
	 * @return
	 */
	public static String md5(String passwordToHash, byte[] salt) {
		String generatedPassword = null;
		try {
			// Create MessageDigest instance for MD5
			MessageDigest md = MessageDigest.getInstance("MD5");
			// Add password bytes to digest
			if (salt != null) {
				md.update(salt);
			}
			// Get the hash's bytes
			byte[] bytes = md.digest(passwordToHash.getBytes());
			// This bytes[] has bytes in decimal format;
			// Convert it to hexadecimal format
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			// Get complete hashed password in hex format
			generatedPassword = sb.toString();
		}
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return generatedPassword;
	}

	/**
	 * SHA256的散列加密算法,256位，32个字节，64个16进制数.
	 * @param inputStr 明文
	 * @return .
	 */
	public static String sha(String inputStr) {
		notNull(inputStr);
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(inputStr.getBytes());

			// 将字节数组转换为十六进制字符串
			StringBuilder hexString = new StringBuilder();
			for (byte b : hash) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1) {
					hexString.append('0');
				}
				hexString.append(hex);
			}
			return hexString.toString();
		}
		catch (NoSuchAlgorithmException ex) {
			System.err.println("不支持sha-256算法");
		}
		return null;
	}

}
