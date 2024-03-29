package com.lind.common.encrypt;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;
import java.util.Base64;

/**
 * DES对称加密算法
 */
public class DESCbcUtils {

	/**
	 * 偏移变量，固定占8位字节.
	 */
	private static final String IV_PARAMETER = "12345678";

	/**
	 * 密钥算法.
	 */
	private static final String ALGORITHM = "DES";

	/**
	 * 加密/解密算法-工作模式-填充模式.
	 */
	private static final String CIPHER_ALGORITHM = "DES/CBC/PKCS5Padding";

	/**
	 * 默认编码.
	 */
	private static final String DM_DEFAULT_ENCODING = "utf-8";

	/**
	 * 生成key.
	 */
	private static Key generateKey(String password) throws Exception {
		DESKeySpec dks = new DESKeySpec(password.getBytes(DM_DEFAULT_ENCODING));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
		return keyFactory.generateSecret(dks);
	}

	/**
	 * DES加密字符串.
	 * @param password 加密密码，长度不能够小于8位
	 * @param data 待加密字符串
	 * @return 加密后内容
	 */
	public static String encrypt(String password, String data) {
		if (password == null || password.length() < 8) {
			throw new RuntimeException("加密失败，key不能小于8位");
		}
		if (data == null) {
			return null;
		}
		try {
			Key secretKey = generateKey(password);
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes(DM_DEFAULT_ENCODING));
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
			byte[] bytes = cipher.doFinal(data.getBytes(DM_DEFAULT_ENCODING));
			return new String(Base64.getEncoder().encode(bytes), DM_DEFAULT_ENCODING);

		}
		catch (Exception e) {
			e.printStackTrace();
			return data;
		}
	}

	/**
	 * DES解密字符串.
	 * @param password 解密密码，长度不能够小于8位
	 * @param data 待解密字符串
	 * @return 解密后内容
	 */
	public static String decrypt(String password, String data) {
		if (password == null || password.length() < 8) {
			throw new RuntimeException("加密失败，key不能小于8位");
		}
		if (data == null) {
			return null;
		}
		try {
			Key secretKey = generateKey(password);
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes(DM_DEFAULT_ENCODING));
			cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
			return new String(cipher.doFinal(Base64.getDecoder().decode(data.getBytes(DM_DEFAULT_ENCODING))),
					DM_DEFAULT_ENCODING);
		}
		catch (Exception e) {
			return null;
		}
	}

}