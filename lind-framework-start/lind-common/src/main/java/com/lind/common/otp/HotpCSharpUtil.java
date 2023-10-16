package com.lind.common.otp;

import org.apache.commons.codec.binary.Base32;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * 与c#的totp算法对应.
 *
 * @author lind
 * @date 2023/10/16 10:14
 * @since 1.0.0
 */
public class HotpCSharpUtil {

	/**
	 * 生成totp.
	 * @param base32Key 密钥
	 * @param timeStep 时间步长（秒）
	 * @param digits 位数
	 * @return
	 * @throws Exception
	 */
	public static String generateTOTP(String base32Key, int timeStep, int digits) throws Exception {
		long counter = (System.currentTimeMillis() / 1000) / timeStep;
		byte[] key = new Base32().decode(base32Key);

		SecretKeySpec secretKey = new SecretKeySpec(key, "HmacSHA1");
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(secretKey);

		byte[] counterBytes = new byte[8];
		for (int i = 0; i < 8; i++) {
			counterBytes[7 - i] = (byte) (counter >> (8 * i));
		}

		byte[] hash = mac.doFinal(counterBytes);
		int offset = hash[hash.length - 1] & 0x0F;
		int binary = ((hash[offset] & 0x7F) << 24 | (hash[offset + 1] & 0xFF) << 16 | (hash[offset + 2] & 0xFF) << 8
				| (hash[offset + 3] & 0xFF));

		int otp = binary % (int) Math.pow(10, digits);
		return String.format("%0" + digits + "d", otp);
	}

}
