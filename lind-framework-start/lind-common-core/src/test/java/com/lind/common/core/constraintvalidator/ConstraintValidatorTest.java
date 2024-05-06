package com.lind.common.core.constraintvalidator;

import com.lind.common.core.util.VerifyCodeUtils;
import org.junit.Test;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;

/**
 * @author lind
 * @date 2023/7/20 16:32
 * @since 1.0.0
 */

public class ConstraintValidatorTest {

	@Test(expected = ConstraintViolationException.class)
	public void validUserEmail() {
		User user = new User();
		user.setEmail("123@sina.com");
		user.setSex(0);
		// 创建一个验证器工厂
		VerifyCodeUtils.BeanValidatorUtils.validateWithException(user);
		user.setEmail("123@sinacom");
		user.setSex(2);
		VerifyCodeUtils.BeanValidatorUtils.validateWithException(user);
		System.out.println(user.getEmail());
	}

	@Test(expected = ConstraintViolationException.class)
	public void validUserSex() {
		User user = new User();
		user.setSex(3);
	}

	@Test
	public void valid2N() {
		User user = new User();
		user.setSex(1);
		user.setEmail("bfyxzls@sina.com");
		user.setPermission(Arrays.asList(1, 2, 4));
		VerifyCodeUtils.BeanValidatorUtils.validateWithException(user);
	}

}
