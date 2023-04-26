package com.lind.common.encrypt;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;

@Slf4j
public class DESTest {

	/**
	 * 生成md5标准的密钥
	 * @param key
	 * @return
	 */
	@SneakyThrows
	static SecretKeySpec getSecretKey(String key) {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		byte[] bkeys = toCsharpBytesHex(md5.digest(key.getBytes())).getBytes();
		byte[] bkeys2 = new byte[8];
		System.arraycopy(bkeys, 0, bkeys2, 0, 8);
		SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "DES");
		return secretKey;
	}

	@SneakyThrows
	private static String encrypt(String data, String key) {

		// 获取key的MD5值
		byte[] md5Bytes = MessageDigest.getInstance("MD5").digest(key.getBytes());

		// md5值转换成十六进制（大写）
		// String md5 = toCsharpBytesHex(md5Bytes).toUpperCase();
		String md5 = toCsharpBytesHex(md5Bytes);
		System.out.println("keyMD5=" + md5);

		// 并获取前8位作为真实的key
		String ivPwd = md5.substring(0, 8);
		System.out.println("ivPwd=" + ivPwd);

		// 使用DES 加密，key和iv都使用pwd
		// 根据pwd，生成DES加密后的密钥，SecretKeySpec对象
		SecretKeySpec secretKey = getSecretKey(key);

		// 根据pwd，创建一个初始化向量IvParameterSpec对象
		IvParameterSpec iv = new IvParameterSpec(ivPwd.getBytes());

		// 创建密码器，参数：算法/模式/填充
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		// 用key和iv初始化密码器，参1：opmode，操作模式-加密、解密等。
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

		// 执行（加密）并返回结果（字节数组）
		byte[] resultBytes = cipher.doFinal(data.getBytes("UTF-8"));

		// 转换成十六进制（大写）
		return new String(java.util.Base64.getEncoder().encode(resultBytes));

	}

	/**
	 * 解密
	 * @param data 要解密的数据
	 * @param key 密钥
	 */
	private static String decrypt(String data, String key) {
		try {
			// 把加密的十六进制字符串数据转换成字节数组
			int len = data.length() >> 1;
			byte[] dataBytes = new byte[len];
			for (int i = 0; i < len; i++) {
				int index = i << 1;
				dataBytes[i] = (byte) Integer.parseInt(data.substring(index, index + 2), 16);
			}

			// 获取key的MD5值
			byte[] md5Bytes = MessageDigest.getInstance("MD5").digest(key.getBytes());
			String pwd = toCsharpBytesHex(md5Bytes).toUpperCase().substring(0, 8);

			// 创建key和iv
			SecretKeySpec secretKey = new SecretKeySpec(pwd.getBytes(), "DES");
			// iv可以用随机数，对结果无影响
			IvParameterSpec iv = new IvParameterSpec(pwd.getBytes());

			// DES 解密
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
			byte[] resultBytes = cipher.doFinal(dataBytes);

			return new String(java.util.Base64.getEncoder().encode(resultBytes));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 字节数组转换成十六进制字符串
	 */
	private static String toCsharpBytesHex(byte[] bytes) {
		if (bytes == null) {
			return null;
		}
		StringBuilder resultSB = new StringBuilder();
		for (byte b : bytes) {
			String hex;
			hex = Integer.toHexString(b & 0xFF);
			if (hex.length() < 2) {
				resultSB.append("0");
			}
			resultSB.append(hex);
		}
		return resultSB.toString();
	}

	@Test
	public void testCBC() {
		log.debug(DESCbcUtils.decrypt("B1D2Y3H4", "1Dpdhoxk4Jg="));
	}

	@SneakyThrows
	@Test
	public void ecb2() {
		log.info(DESEcbUtils.encrypt("123456", "keyphrase"));
	}

	@SneakyThrows
	@Test
	public void ecb3() {
		String msg = DESNetUtils.EncryptUtil.encryptToBase64("123456", "keyphrase");
		log.info("ens={}", msg);
		log.info(DESNetUtils.EncryptUtil.decryptFromBase64(msg, "keyphrase"));
	}

}
