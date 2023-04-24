package com.lind.common.util;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

/**
 * JasyptUtils加密.
 */
@Slf4j
public class JasyptUtils {

	/**
	 * Jasypt生成加密结果.
	 * @param password 配置文件中设定的加密密码 jasypt.encryptor.password
	 * @param value 待加密值
	 * @return .
	 */
	public static String encyptPwd(String password, String value) {
		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		encryptor.setConfig(cryptor(password));
		String result = encryptor.encrypt(value);
		return result;
	}

	/**
	 * 解密.
	 * @param password 配置文件中设定的加密密码 jasypt.encryptor.password
	 * @param value 待解密密文
	 * @return .
	 */
	public static String decyptPwd(String password, String value) {
		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		encryptor.setConfig(cryptor(password));
		String result = encryptor.decrypt(value);
		return result;
	}

	/**
	 * 加密.
	 * @param password 密码
	 * @return .
	 */
	private static SimpleStringPBEConfig cryptor(String password) {
		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		config.setPassword(password);
		config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
		config.setKeyObtentionIterations("1000");
		config.setPoolSize(1);
		config.setProviderName("SunJCE");
		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
		config.setStringOutputType("base64");
		return config;
	}

}
