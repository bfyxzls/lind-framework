package com.lind.common.encrypt;

import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * @author lind
 * @date 2024/5/7 17:37
 * @since 1.0.0
 */
public class PBKDF2SHA256Test {

	@Test
	public void generatePassword() {
		String rawPassword = "123456";// 原密码
		byte[] salt = PBKDF2SHAUtils.getSalt();// 随机盐，需要存储
		String encodePass = PBKDF2SHAUtils.encodedCredential(rawPassword, salt);// 秘文，需要存储
		System.out.println("encodePass:" + encodePass);
		String formData = "123456"; // 表单数据
		Assert.equals(encodePass, PBKDF2SHAUtils.encodedCredential(formData, salt));// 与库里密码对比

	}

}
