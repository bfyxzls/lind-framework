package com.lind.common.encrypt;

import lombok.SneakyThrows;
import org.apache.commons.net.util.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.Security;

/**
 * 兼容.net的DES-ECB模块，密钥为16字节的md5值.
 */
public class DESNetUtils {

	public static class EncryptUtil {

		private static final String ALGORITHM = "DESede";

		private static final String CIPHER_TRANSFER = "DESede/ECB/PKCS5Padding";

		private static final String ENCODING = "UTF-8";

		static {
			init();
		}

		public static void init() {
			BouncyCastleProvider bouncyCastleProvider = new BouncyCastleProvider();
			Security.addProvider(bouncyCastleProvider);
		}

		/**
		 * 初始化key.
		 * @param key
		 */
		@SneakyThrows
		static SecretKey getSecretKey(String key) {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] bkeys = md5.digest(key.getBytes());
			SecretKey secretKey = new SecretKeySpec(bkeys, ALGORITHM);
			return secretKey;
		}

		/**
		 * 字符串加密.
		 * @param plainText
		 * @param key
		 * @return
		 * @throws Exception
		 */
		public static String encryptToBase64(String plainText, String key) throws Exception {
			SecretKey secretKey = getSecretKey(key);
			Cipher cipher = Cipher.getInstance(CIPHER_TRANSFER);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] result = cipher.doFinal(plainText.getBytes(ENCODING));
			String s1 = Base64.encodeBase64String(result);
			return s1;
		}

		/**
		 * 字节数组加密.
		 * @param plainText
		 * @param key
		 * @return
		 * @throws Exception
		 */
		public static byte[] encryptToByte(byte[] plainText, String key) throws Exception {
			SecretKey secretKey = getSecretKey(key);
			Cipher c1 = Cipher.getInstance(CIPHER_TRANSFER);
			c1.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] result = c1.doFinal(plainText);
			return result;
		}

		/**
		 * 字节数组解密.
		 * @param cipherText
		 * @param key
		 * @return
		 * @throws Exception
		 */
		public static byte[] decryptFromByte(byte[] cipherText, String key) throws Exception {
			SecretKey secretKey = getSecretKey(key);
			Cipher c1 = Cipher.getInstance(CIPHER_TRANSFER);
			c1.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] result = c1.doFinal(cipherText);
			return result;
		}

		/**
		 * 字符串解密.
		 * @param base64
		 * @param key
		 * @return
		 * @throws Exception
		 */
		public static String decryptFromBase64(String base64, String key) throws Exception {
			SecretKey secretKey = getSecretKey(key);
			Cipher c1 = Cipher.getInstance(CIPHER_TRANSFER);
			c1.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] result = c1.doFinal(Base64.decodeBase64(base64));
			return new String(result, ENCODING);
		}

	}

}
