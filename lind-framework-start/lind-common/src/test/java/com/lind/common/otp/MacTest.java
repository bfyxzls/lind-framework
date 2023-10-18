package com.lind.common.otp;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Arrays;

/**
 * @author lind
 * @date 2023/10/17 9:49
 * @since 1.0.0
 */
public class MacTest {

	public static void main(String[] args) throws Exception {
		// 1. 选择一个MAC算法（这里使用HMAC-SHA256）
		String algorithm = "HmacSHA256";

		// 2. 准备密钥
		String secretKey = "lind"; // 替换成你的密钥
		Key key = new SecretKeySpec(secretKey.getBytes(), algorithm);

		// 3. 创建MAC对象并初始化
		Mac mac = Mac.getInstance(algorithm);
		mac.init(key);

		// 4. 要验证的消息
		String message = "This is a message to be verified.";

		// 5. 计算MAC
		byte[] macBytes = mac.doFinal(message.getBytes());

		// 6. 发送消息和MAC给接收方
		// 接收方将使用相同的密钥和消息来计算MAC，并比较它与接收到的MAC是否匹配

		// 模拟接收方验证
		boolean isValid = verifyMAC(key, message, macBytes);
		if (isValid) {
			System.out.println("消息完整性验证通过。");
		}
		else {
			System.out.println("消息完整性验证失败。");
		}
	}

	// 验证MAC的方法
	public static boolean verifyMAC(Key key, String message, byte[] receivedMAC) throws Exception {
		Mac mac = Mac.getInstance(key.getAlgorithm());
		mac.init(key);
		byte[] calculatedMAC = mac.doFinal(message.getBytes());

		// 使用MessageDigest的isEqual方法来比较两个MAC是否相等
		return Arrays.equals(calculatedMAC, receivedMAC);
	}

}
