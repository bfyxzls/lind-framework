package com.lind.common.encrypt;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * PB KDF2 SHA工具类
 *
 * @author lind
 * @date 2024/5/8 8:20
 * @since 1.0.0
 */
public class PBKDF2SHAUtils {

	private static final String PBKDF_2_WITH_HMAC_SHA_512 = "PBKDF2WithHmacSHA512";

	private static final int ITERATIONS = 27500;

	private static final int DERIVED_KEY_SIZE = 512;

	/**
	 * PB KDF2 SHA256加密
	 * @param rawPassword
	 * @param salt
	 * @return
	 */
	public static String encodedCredential(String rawPassword, byte[] salt) {
		return encodedCredential(rawPassword, ITERATIONS, salt, DERIVED_KEY_SIZE);
	}

	// 加密
	public static String encodedCredential(String rawPassword, int iterations, byte[] salt, int derivedKeySize) {
		KeySpec spec = new PBEKeySpec(rawPassword.toCharArray(), salt, iterations, derivedKeySize);

		try {
			byte[] key = getSecretKeyFactory().generateSecret(spec).getEncoded();
			return new String(Base64.getEncoder().encode(key));
		}
		catch (InvalidKeySpecException e) {
			throw new RuntimeException("Credential could not be encoded", e);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// 随机盐
	public static byte[] getSalt() {
		byte[] buffer = new byte[16];
		SecureRandom secureRandom = new SecureRandom();
		secureRandom.nextBytes(buffer);
		return buffer;
	}

	private static SecretKeyFactory getSecretKeyFactory() {
		try {
			return SecretKeyFactory.getInstance(PBKDF_2_WITH_HMAC_SHA_512);
		}
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("PBKDF2 algorithm not found", e);
		}
	}

}
