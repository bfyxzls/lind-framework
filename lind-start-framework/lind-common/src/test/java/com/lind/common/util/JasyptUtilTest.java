package com.lind.common.util;

import com.lind.common.util.JasyptUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JasyptUtilTest {

	@Value("${lind.password}")
	private String password;

	@Test
	public void encryption() {
		String pwd = JasyptUtils.encyptPwd("xboot", "123456");
		// 加密
		System.out.println(pwd);
		// 解密
		System.out.println(JasyptUtils.decyptPwd("xboot", pwd));
		// 断言
		Assert.assertEquals("123456", JasyptUtils.decyptPwd("xboot", pwd));
	}

	@Test
	public void fail() {
		String b = "2";
		Assert.assertEquals("1", b);
	}

	@Test
	public void yml() {
		System.out.println("lind.password=" + password);
	}

}
