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
	 * @param key 共享密钥，字符串对应的byte数组
	 * @param timeStep 时间步长（秒）
	 * @param digits 位数
	 * @return
	 * @throws Exception
	 */
	public static String generateTOTP(byte[] key, int timeStep, int digits) throws Exception {
		return generateTOTP(new Base32().encodeToString(key), timeStep, digits);
	}

	/**
	 * 生成totp.
	 * @param base32Key 共享密钥,32位字符串
	 * @param timeStep 时间步长（秒）
	 * @param digits 位数
	 * @return
	 * @throws Exception
	 */
	public static String generateTOTP(String base32Key, int timeStep, int digits) throws Exception {
		/**
		 * 1. 服务器和客户端都知道共享的密钥。 2. 客户端获取当前时间戳，通常是以秒为单位。 3.
		 * 客户端将当前时间戳除以时间步长并取整，以获得一个时间窗口（Time Window）的序号。 4.
		 * 客户端使用哈希函数（如HMAC-SHA1）将密钥和时间窗口的序号作为输入来生成哈希值。 5. 从哈希值中提取指定的位数作为一次性密码，通常是6位数字。 6.
		 * 客户端将生成的一次性密码显示给用户，用户输入该密码进行身份验证。 7. 服务器使用相同的密钥和时间戳计算一次性密码，以验证用户输入的密码是否匹配。
		 */
		if (base32Key.length() != 32) {
			throw new Exception("base32Key length must be 32 characters");
		}
		long counter = (System.currentTimeMillis() / 1000) / timeStep; // 生成时间窗口,30秒之内生产的counter是一样的
		byte[] key = new Base32().decode(base32Key); // 使用安全的base32解码
		SecretKeySpec secretKey = new SecretKeySpec(key, "HmacSHA1"); // 使用HmacSHA1算法
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(secretKey);

		// 定义8个字节的数组,用来存放counter，事实上，相当于将长型转换成byte数组.
		byte[] counterBytes = new byte[8];
		for (int i = 0; i < 8; i++) {
			counterBytes[7 - i] = (byte) (counter >> (8 * i));
		}

		byte[] hash = mac.doFinal(counterBytes); // HMAC带有密钥+counterBytes计算哈希值，长度是20
		int offset = hash[hash.length - 1] & 0x0F; // 从HMAC哈希值中提取一个偏移值（offset）
		int binary = ((hash[offset] & 0x7F) << 24 | (hash[offset + 1] & 0xFF) << 16 | (hash[offset + 2] & 0xFF) << 8
				| (hash[offset + 3] & 0xFF)); // 通过偏移量进行截取，截取4个字节，然后转换成整型

		int otp = binary % (int) Math.pow(10, digits); // 生成指定位数的数字
		return String.format("%0" + digits + "d", otp);
	}

}
