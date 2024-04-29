package com.lind.common.encrypt;

import org.junit.Test;

import java.io.UnsupportedEncodingException;

/**
 * @author lind
 * @date 2024/4/29 9:20
 * @since 1.0.0
 */
public class MD5_Sha256 {

	static String PARAMS = "总的来说，虽然MD5和SHA-256都是哈希算法，但由于MD5存在碰撞风险.";

	@Test
	public void md5() {
		System.out.println(HashUtils.md5(PARAMS));
		//61b1c1c32fb2addff0cf3abbf95b7e4b
	}

	@Test
	public void sha256() throws UnsupportedEncodingException {
		System.out.println(HashUtils.sha(PARAMS));
		//14ed6fba8cff4d97a372401a8c2feb9a6dd5e505a28283943a326952577100d1
	}

}
