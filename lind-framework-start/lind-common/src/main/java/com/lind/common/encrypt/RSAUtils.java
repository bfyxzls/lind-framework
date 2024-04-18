package com.lind.common.encrypt;

import lombok.SneakyThrows;
import org.apache.commons.net.util.Base64;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Base64Utils;
import org.springframework.util.ResourceUtils;

import javax.crypto.Cipher;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA非对称加密算法. 公钥加密，私钥解密；多人拥有公钥，可以加密，一个拥有私钥，完成解密; 密钥加签，公钥验签；多人拥有公钥，可以签名，一个拥有私钥，完成验证签名。
 */
public class RSAUtils {

	/**
	 * 签名算法 MD5WithRSA.
	 */
	public static final String SIGN_ALGORITHMS = "MD5WithRSA";

	/**
	 * 签名算法 SHA256WithRSA.
	 */
	public static final String SIGN_ALGORITHMS_SHA256 = "SHA256withRSA";

	/**
	 * RSA最大加密的块.
	 */
	private static final int MAX_ENCRYPT_BLOCK = 501;

	/**
	 * RSA最大解密的块，这个与加密块要对应，比加密块大11.
	 */
	private static final int MAX_DECRYPT_BLOCK = 512;

	/**
	 * 算法.
	 */
	private static final String ALGORITHM_NAME = "RSA";

	/**
	 * 密钥长度.
	 */
	private static final int MAX = 4096; // 根据你的加密升序去设置:1024(MAX_ENCRYPT_BLOCK:117,MAX_DECRYPT_BLOCK:128),2048(MAX_ENCRYPT_BLOCK:245,MAX_DECRYPT_BLOCK:256),4096(MAX_ENCRYPT_BLOCK:501,MAX_DECRYPT_BLOCK:512)

	/**
	 * 保存公钥和私钥.
	 */
	@SneakyThrows
	public static void save(KeyPair keyPair) {

		String PUBLIC_KEY_FILE = ResourceUtils.getURL("classpath:").getPath() + "key.public";
		String PRIVATE_KEY_FILE = ResourceUtils.getURL("classpath:").getPath() + "key.private";
		try (Writer writer = new BufferedWriter(new FileWriter(PUBLIC_KEY_FILE))) {
			writer.write(Base64.encodeBase64String(keyPair.getPublic().getEncoded()));
		}

		try (Writer writer = new BufferedWriter(new FileWriter(PRIVATE_KEY_FILE))) {
			writer.write(Base64.encodeBase64String(keyPair.getPrivate().getEncoded()));
		}
	}

	@SneakyThrows
	public static PrivateKey loadPrivateKey(String file) {
		ClassPathResource classPathResource = new ClassPathResource(file);
		InputStream inputStream = classPathResource.getInputStream();
		String result = new String(readInputStream(inputStream));
		return getPrivateKey(result);
	}

	/**
	 * 读取输入到到一个byte[]里.
	 * @param inputStream
	 * @return
	 */
	@SneakyThrows
	static byte[] readInputStream(InputStream inputStream) {
		byte[] buffer = new byte[MAX];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		bos.close();
		return bos.toByteArray();
	}

	@SneakyThrows
	public static PublicKey loadPublicKey(String file) {
		ClassPathResource classPathResource = new ClassPathResource(file);
		InputStream inputStream = classPathResource.getInputStream();
		return getPublicKey(new String(readInputStream(inputStream)));
	}

	/**
	 * 获取密钥对
	 * @return java.security.KeyPair
	 */
	public static KeyPair getKeyPair() throws Exception {
		KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM_NAME);
		generator.initialize(MAX);
		return generator.generateKeyPair();
	}

	/**
	 * 获取私钥
	 * @param privateKey 私钥字符串
	 * @return java.security.PrivateKey
	 */
	public static PrivateKey getPrivateKey(String privateKey) throws Exception {
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_NAME);
		byte[] decodedKey = Base64Utils.decode(privateKey.getBytes());
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
		return keyFactory.generatePrivate(keySpec);

	}

	/**
	 * 获取公钥
	 * @param publicKey 公钥字符串
	 * @return java.security.PublicKey
	 */
	public static PublicKey getPublicKey(String publicKey) throws Exception {
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_NAME);
		byte[] decodedKey = Base64Utils.decode(publicKey.getBytes());
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
		return keyFactory.generatePublic(keySpec);

	}

	/**
	 * RSA加密
	 * @param data 待加密数据
	 * @param publicKey 公钥
	 * @return java.lang.String
	 */
	public static String encrypt(String data, PublicKey publicKey) throws Exception {
		Cipher cipher = Cipher.getInstance(ALGORITHM_NAME);
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		int inputLen = data.getBytes().length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offset = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段加密
		while (inputLen - offset > 0) {
			if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
				cache = cipher.doFinal(data.getBytes(), offset, MAX_ENCRYPT_BLOCK);
			}
			else {
				cache = cipher.doFinal(data.getBytes(), offset, inputLen - offset);
			}
			out.write(cache, 0, cache.length);
			i++;
			offset = i * MAX_ENCRYPT_BLOCK;
		}
		byte[] encryptedData = out.toByteArray();
		out.close();
		// 获取加密内容使用base64进行编码,并以UTF-8为标准转化成字符串
		// 加密后的字符串
		return new String(Base64Utils.encode(encryptedData));
	}

	/**
	 * RSA解密
	 * @param data 待解密数据
	 * @param privateKey 私钥
	 * @return java.lang.String
	 */
	public static String decrypt(String data, PrivateKey privateKey) throws Exception {
		Cipher cipher = Cipher.getInstance(ALGORITHM_NAME);
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] dataBytes = Base64.decodeBase64(data);
		int inputLen = dataBytes.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offset = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段解密
		while (inputLen - offset > 0) {
			if (inputLen - offset > MAX_DECRYPT_BLOCK) {
				cache = cipher.doFinal(dataBytes, offset, MAX_DECRYPT_BLOCK);
			}
			else {
				cache = cipher.doFinal(dataBytes, offset, inputLen - offset);
			}
			out.write(cache, 0, cache.length);
			i++;
			offset = i * MAX_DECRYPT_BLOCK;
		}
		byte[] decryptedData = out.toByteArray();
		out.close();
		// 解密后的内容
		return new String(decryptedData, StandardCharsets.UTF_8);
	}

	/**
	 * 签名,会用到签名算法
	 * @param data 待签名数据
	 * @param privateKey 私钥
	 * @return java.lang.String
	 */
	public static String sign(String data, PrivateKey privateKey) throws Exception {
		byte[] keyBytes = privateKey.getEncoded();
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_NAME);
		PrivateKey key = keyFactory.generatePrivate(keySpec);
		Signature signature = Signature.getInstance(SIGN_ALGORITHMS_SHA256);
		signature.initSign(key);
		signature.update(data.getBytes(StandardCharsets.UTF_8));
		return new String(Base64Utils.encode(signature.sign()));
	}

	/**
	 * 验签,会用到签名算法
	 * @param srcData 原始字符串
	 * @param publicKey 公钥
	 * @param sign 签名
	 * @return boolean 是否验签通过
	 */
	public static boolean verify(String srcData, PublicKey publicKey, String sign) throws Exception {
		byte[] keyBytes = publicKey.getEncoded();
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_NAME);
		PublicKey key = keyFactory.generatePublic(keySpec);
		Signature signature = Signature.getInstance(SIGN_ALGORITHMS_SHA256);
		signature.initVerify(key);
		signature.update(srcData.getBytes(StandardCharsets.UTF_8));
		try {
			return signature.verify(Base64Utils.decodeFromString(sign));
		}
		catch (Exception ex) {
			return signature.verify(Base64Utils.decodeFromUrlSafeString(sign));
		}
	}

}
