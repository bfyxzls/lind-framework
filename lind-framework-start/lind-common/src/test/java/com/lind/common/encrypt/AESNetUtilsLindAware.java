package com.lind.common.encrypt;

import com.lind.common.zip.LZString;
import org.junit.Test;

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

}
