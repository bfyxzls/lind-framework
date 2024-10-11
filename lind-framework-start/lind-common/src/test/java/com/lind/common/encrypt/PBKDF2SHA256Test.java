package com.lind.common.encrypt;

import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

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

	@Test
	public void generateDbPassword() {
		String db = "grmrRyYqSELUyTq8XMd39eu50qi8TvwsHmyekMOd7i7vYq8rblHZbMwzno4Zv5oYpma0PpeIaVlmzBthZVKSyQ==";
		byte[] salt = "u7TEM5yNN28WYn7oZCRdqQ==".getBytes();
		String password = "123456".toLowerCase();
		String encodePass = PBKDF2SHAUtils.encodedCredential(password, salt);// 秘文，需要存储
		Assert.equals(db, encodePass);
	}

}
