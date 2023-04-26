package com.lind.common.encrypt;

import lombok.SneakyThrows;
import org.apache.commons.net.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;

/**
 * DES-ECB模式.
 */
public class DESEcbUtils {

	/**
	 * 加密数据
	 * @param data
	 * @param password
	 * @return
	 * @throws Exception
	 */

	@SneakyThrows
	public static String encrypt(String password, String data) {
		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(getKey(password), "DES"));
		byte[] encryptedData = cipher.doFinal(data.getBytes("UTF-8"));
		return Base64.encodeBase64String(encryptedData);
	}

	/**
	 * key为md5值的前8位.
	 * @param key
	 * @return
	 */
	@SneakyThrows
	static byte[] getKey(String key) {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		byte[] bkeys = md5.digest(key.getBytes());
		byte[] bkeys2 = new byte[8];
		System.arraycopy(bkeys, 0, bkeys2, 0, 8);
		return bkeys2;
	}

	/***
	 * 解密数据
	 * @param data
	 * @param password
	 * @return
	 * @throws Exception
	 */

	@SneakyThrows
	public static String decrypt(String password, String data) {

		byte[] sourceBytes = Base64.decodeBase64(data);
		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(getKey(password), "DES"));
		byte[] decoded = cipher.doFinal(sourceBytes);
		return new String(decoded, "UTF-8");

	}

}
