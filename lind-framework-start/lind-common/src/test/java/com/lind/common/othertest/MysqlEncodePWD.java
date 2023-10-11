package com.lind.common.othertest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;

/**
 * 读取navicat保存的密码.
 *
 * @author lind
 * @date 2023/2/20 13:42
 * @since 1.0.0
 */
public class MysqlEncodePWD {

	public static void main(String[] args) {
		// navicat11解密
		Navicat11Cipher de = new Navicat11Cipher();
		String password = "770F8DA8E9978CAF98";
		System.out.println(de.decryptString(password));
		// navicat12+解密
		// Navicat12Cipher de12 = new Navicat12Cipher();
		// System.out.println(de12.decryptString("503AA930968F877F04770B47DD731DC0"));
	}

	static class Navicat11Cipher {

		public static final String DefaultUserKey = "3DC5CA39";

		private static byte[] _IV;

		private static SecretKeySpec _Key;

		private static Cipher _Encryptor;

		private static Cipher _Decryptor;

		static {
			initKey(DefaultUserKey);
			initChiperEncrypt();
			initChiperDecrypt();
			initIV();
		}

		private static void initKey(String UserKey) {
			try {
				MessageDigest sha1 = MessageDigest.getInstance("SHA1");
				byte[] userkey_data = UserKey.getBytes(StandardCharsets.UTF_8);
				sha1.update(userkey_data, 0, userkey_data.length);
				_Key = new SecretKeySpec(sha1.digest(), "Blowfish");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

		private static void initChiperEncrypt() {
			try {
				// Must use NoPadding
				_Encryptor = Cipher.getInstance("Blowfish/ECB/NoPadding");
				_Encryptor.init(Cipher.ENCRYPT_MODE, _Key);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

		private static void initChiperDecrypt() {
			try {
				// Must use NoPadding
				_Decryptor = Cipher.getInstance("Blowfish/ECB/NoPadding");
				_Decryptor.init(Cipher.DECRYPT_MODE, _Key);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

		private static void initIV() {
			try {
				byte[] initVec = DatatypeConverter.parseHexBinary("FFFFFFFFFFFFFFFF");
				_IV = _Encryptor.doFinal(initVec);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void xorBytes(byte[] a, byte[] b) {
			for (int i = 0; i < a.length; i++) {
				int aVal = a[i] & 0xff; // convert byte to integer
				int bVal = b[i] & 0xff;
				a[i] = (byte) (aVal ^ bVal); // xor aVal and bVal and typecast to byte
			}
		}

		private void xorBytes(byte[] a, byte[] b, int l) {
			for (int i = 0; i < l; i++) {
				int aVal = a[i] & 0xff; // convert byte to integer
				int bVal = b[i] & 0xff;
				a[i] = (byte) (aVal ^ bVal); // xor aVal and bVal and typecast to byte
			}
		}

		private byte[] Encrypt(byte[] inData) {
			try {
				byte[] CV = Arrays.copyOf(_IV, _IV.length);
				byte[] ret = new byte[inData.length];

				int blocks_len = inData.length / 8;
				int left_len = inData.length % 8;

				for (int i = 0; i < blocks_len; i++) {
					byte[] temp = Arrays.copyOfRange(inData, i * 8, (i * 8) + 8);

					xorBytes(temp, CV);
					temp = _Encryptor.doFinal(temp);
					xorBytes(CV, temp);

					System.arraycopy(temp, 0, ret, i * 8, 8);
				}

				if (left_len != 0) {
					CV = _Encryptor.doFinal(CV);
					byte[] temp = Arrays.copyOfRange(inData, blocks_len * 8, (blocks_len * 8) + left_len);
					xorBytes(temp, CV, left_len);
					System.arraycopy(temp, 0, ret, blocks_len * 8, temp.length);
				}

				return ret;
			}
			catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		public String encryptString(String inputString) {
			try {
				byte[] inData = inputString.getBytes(StandardCharsets.UTF_8);
				byte[] outData = Encrypt(inData);
				return DatatypeConverter.printHexBinary(outData);
			}
			catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}

		private byte[] Decrypt(byte[] inData) {
			try {
				byte[] CV = Arrays.copyOf(_IV, _IV.length);
				byte[] ret = new byte[inData.length];

				int blocks_len = inData.length / 8;
				int left_len = inData.length % 8;

				for (int i = 0; i < blocks_len; i++) {
					byte[] temp = Arrays.copyOfRange(inData, i * 8, (i * 8) + 8);

					temp = _Decryptor.doFinal(temp);
					xorBytes(temp, CV);
					System.arraycopy(temp, 0, ret, i * 8, 8);
					for (int j = 0; j < CV.length; j++) {
						CV[j] = (byte) (CV[j] ^ inData[i * 8 + j]);
					}
				}

				if (left_len != 0) {
					CV = _Encryptor.doFinal(CV);
					byte[] temp = Arrays.copyOfRange(inData, blocks_len * 8, (blocks_len * 8) + left_len);

					xorBytes(temp, CV, left_len);
					System.arraycopy(temp, 0, ret, blocks_len * 8 + 0, temp.length);
				}

				return ret;
			}
			catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		public String decryptString(String hexString) {
			try {
				byte[] inData = DatatypeConverter.parseHexBinary(hexString);
				byte[] outData = Decrypt(inData);
				return new String(outData, StandardCharsets.UTF_8);
			}
			catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}

	}

	static class Navicat12Cipher {

		private static final SecretKeySpec _AesKey;

		private static final IvParameterSpec _AesIV;

		static {
			_AesKey = new SecretKeySpec("libcckeylibcckey".getBytes(StandardCharsets.UTF_8), "AES");
			_AesIV = new IvParameterSpec("libcciv libcciv ".getBytes(StandardCharsets.UTF_8));
		}

		public String encryptString(String plaintext) {
			try {
				Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
				cipher.init(Cipher.ENCRYPT_MODE, _AesKey, _AesIV);
				byte[] ret = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
				return DatatypeConverter.printHexBinary(ret);
			}
			catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}

		public String decryptString(String ciphertext) {
			try {
				Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
				cipher.init(Cipher.DECRYPT_MODE, _AesKey, _AesIV);
				byte[] ret = cipher.doFinal(DatatypeConverter.parseHexBinary(ciphertext));
				return new String(ret, StandardCharsets.UTF_8);
			}
			catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}

	}

}
