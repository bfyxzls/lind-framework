package com.lind.common.encrypt;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 适合于.net和java共同的AES加密,24个字符
 */
public class AESNetUtils {

	static final Logger log = LoggerFactory.getLogger(AESNetUtils.class);

	/**
	 * 加密.
	 * @param content
	 * @param key
	 * @return
	 * @throws Exception
	 */
	@SneakyThrows
	public static String encrypt(String content, String key) {
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
	@SneakyThrows
	public static String decrypt(String content, String key) {
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
			String result = HashUtils.encryptBASE64(encrypted);
			log.info("encrypt:{}", result);
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
			byte[] encrypted1 = HashUtils.decryptBASE64(data);
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
			IvParameterSpec ivspec = new IvParameterSpec(IV.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
			byte[] original = cipher.doFinal(encrypted1);
			String originalString = new String(original, "UTF-8");
			log.info("decrypt:{}", originalString);
			return originalString;
		}
		catch (Exception e) {
			throw e;
		}
	}

}
