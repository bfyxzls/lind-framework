package com.lind.uaa.jwt.utils;

import com.lind.uaa.jwt.config.PermitAllUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * KeyPairGenerator生成器，目前存储在内存里，如果多应用共用，应该把它存储到磁盘上，对应用端公开publicKey来完成签名的验证过程.
 * 使用SecretKeyUtils.main方法生成公私钥并存储到resources目录 公钥公开，用于进行签名验证
 */
public class SecretKeyUtils {

	public static final String KEY_ALGORITHM = "RSA";

	private static final String PUBLIC_KEY = "RSAPublicKey";

	private static final String PRIVATE_KEY = "RSAPrivateKey";

	private static final Logger logger = LoggerFactory.getLogger(PermitAllUrl.class);

	private static RSA256Key rsa256Key;

	// 获得公钥
	public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
		// 获得map中的公钥对象 转为key对象
		Key key = (Key) keyMap.get(PUBLIC_KEY);
		return encryptBASE64(key.getEncoded());
	}

	public static String getPublicKey(RSA256Key rsa256Key) throws Exception {
		// 获得map中的公钥对象 转为key对象
		Key key = rsa256Key.getPublicKey();
		return encryptBASE64(key.getEncoded());
	}

	// 获得私钥
	public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
		// 获得map中的私钥对象 转为key对象
		Key key = (Key) keyMap.get(PRIVATE_KEY);
		return encryptBASE64(key.getEncoded());
	}

	// 获得私钥
	public static String getPrivateKey(RSA256Key rsa256Key) throws Exception {
		// 获得map中的私钥对象 转为key对象
		Key key = rsa256Key.getPrivateKey();
		return encryptBASE64(key.getEncoded());
	}

	// 解码返回byte
	public static byte[] decryptBASE64(String key) throws Exception {
		return (new BASE64Decoder()).decodeBuffer(key);
	}

	// 编码返回字符串
	public static String encryptBASE64(byte[] key) throws Exception {
		return (new BASE64Encoder()).encodeBuffer(key);
	}

	// 使用KeyPairGenerator 生成公私钥，存放于map对象中
	public static Map<String, Object> initKey() throws Exception {
		/* RSA算法要求有一个可信任的随机数源 */
		// 获得对象 KeyPairGenerator 参数 RSA 1024个字节
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		keyPairGen.initialize(1024);

		// 通过对象 KeyPairGenerator 生成密匙对 KeyPair
		KeyPair keyPair = keyPairGen.generateKeyPair();

		// 通过对象 KeyPair 获取RSA公私钥对象RSAPublicKey RSAPrivateKey
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		// 公私钥对象存入map中
		Map<String, Object> keyMap = new HashMap<String, Object>(2);
		String priKeyBase64 = readFile(ResourceUtils.getFile("classpath:private.key"));
		byte[] encPriKey = new BASE64Decoder().decodeBuffer(priKeyBase64);
		PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(encPriKey);
		PrivateKey privateKey1 = KeyFactory.getInstance("RSA").generatePrivate(priPKCS8);

		String pubKeyBase64 = readFile(ResourceUtils.getFile("classpath:public.key"));
		byte[] encPubKey = new BASE64Decoder().decodeBuffer(pubKeyBase64);
		X509EncodedKeySpec encPubKeySpec = new X509EncodedKeySpec(encPubKey);
		PublicKey publicKey1 = KeyFactory.getInstance("RSA").generatePublic(encPubKeySpec);
		// 这边改成了从文件读取公私钥，以便将公钥公开到其它客户端，以后jwt校验由自己端校验
		keyMap.put(PUBLIC_KEY, publicKey1);
		keyMap.put(PRIVATE_KEY, privateKey1);

		logger.info("\npub:{}\nsub:{}", getPublicKey(keyMap), getPrivateKey(keyMap));
		return keyMap;
	}

	/**
	 * 读取密钥文件
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String readFile(File file) throws IOException {
		InputStream in = null;
		ByteArrayOutputStream out = null;
		try {
			in = new FileInputStream(file);
			out = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int len = -1;
			while ((len = in.read(buf)) != -1) {
				out.write(buf, 0, len);
			}
			out.flush();
			byte[] data = out.toByteArray();
			return new String(data);
		}
		finally {
			close(in);
			close(out);
		}
	}

	public static void close(Closeable c) {
		if (c != null) {
			try {
				c.close();
			}
			catch (IOException e) {
				// nothing
			}
		}
	}

	/**
	 * 获取公私钥
	 * @return
	 * @throws Exception
	 */
	public static synchronized RSA256Key getRSA256Key() throws Exception {
		if (rsa256Key == null) {
			synchronized (RSA256Key.class) {
				if (rsa256Key == null) {
					rsa256Key = new RSA256Key();
					Map<String, Object> map = initKey();
					rsa256Key.setPrivateKey((RSAPrivateKey) map.get(SecretKeyUtils.PRIVATE_KEY));
					rsa256Key.setPublicKey((RSAPublicKey) map.get(SecretKeyUtils.PUBLIC_KEY));

				}
			}
		}
		return rsa256Key;
	}

	public static void main(String[] args) {
		Map<String, Object> keyMap;
		try {
			keyMap = initKey(); // 使用 java.security.KeyPairGenerator 生成 公/私钥
			String publicKey = getPublicKey(keyMap);
			System.out.println("公钥：\n" + publicKey);
			String privateKey = getPrivateKey(keyMap);
			System.out.println("私钥：\n" + privateKey);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}