package com.lind.common.encrypt;

import com.lind.common.zip.LZString;
import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class AESNetUtilsLindAware {

	@Test
	public void aes_base16_lz() {
		String str = "12345678_f_20200612_D_100_V6";
		String key = "225E8C70688FD19C5C01A212302322ED";
		String aes = AESNetUtils.encrypt(str, key);
		String result = LZString.compress(aes);
		System.out.println("code=" + result);
		System.out.println("rle=" + LZString.decompress(result));
	}

	@Test
	public void test2() {
		String code = AESNetUtils.encrypt("123456", "keyphrasekeyphra");
		System.out.println("base64:" + code);
	}

	@Test
	public void aes32() throws Exception {
		String key = "bf64d5aa94224916b34b081027e552f9";
		String pass = "e62afb133fbc1a16179119237e62ec1e";
		String iv = "5485693214587452";
		String code = AESNet32Utils.decryptData(pass, key, iv);
		System.out.println("base64:" + code);
	}

	@Test
	public void aes3test() throws Exception {
		String key = "bdyh@20240805@11";
		String plainText = "e1ad34d0-6d01-40fc-a0d7-fd7f887a068d";
		String iv = "5485693214587452";
		String cipherText = URLEncoder.encode(encrypt(plainText, key));
		System.out.println("cipherText:" + cipherText);

		String code = encrypt(URLDecoder.decode(cipherText), key);
		System.out.println("plainText:" + code);
	}
	public static String encrypt(String content, String key) throws Exception {
		String IV = key;
		if (key.length() > 16) {
			// IV为商户MD5密钥后16位
			IV = key.substring(key.length() - 16);
			// RES的KEY 为商户MD5密钥的前16位
			key = key.substring(0, 16);
		}

		return encryptData(content, key, IV);
	}

	public static String encryptData(String data, String key, String IV) throws Exception {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			byte[] dataBytes = data.getBytes("UTF-8");
			int plaintextLength = dataBytes.length;
			byte[] plaintext = new byte[plaintextLength];
			System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
			SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
			IvParameterSpec ivspec = new IvParameterSpec(IV.getBytes());
			cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
			byte[] encrypted = cipher.doFinal(plaintext);
			String result = bytesToHexString(encrypted);
			return result;
		} catch (Exception e) {
			System.err.println("encryptData解密失败");
			return data;
		}
	}
	private static String bytesToHexString(byte[] src) {
		StringBuilder builder = new StringBuilder();
		if (src == null || src.length <= 0) {
			return null;
		}
		String hv;
		for (int i = 0; i < src.length; i++) {
			hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
			if (hv.length() < 2) {
				builder.append(0);
			}
			builder.append(hv);
		}
		return builder.toString();
	}

}
