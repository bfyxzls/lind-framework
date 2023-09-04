package com.lind.common.core.constraintvalidator;

import com.lind.common.core.util.BeanValidatorUtils;
import org.junit.Test;

/**
 * @author lind
 * @date 2023/7/20 16:32
 * @since 1.0.0
 */

public class ConstraintValidatorTest {

	@Test
	public void validUserEmail() {
		User user = new User();
		user.setEmail("123@sina.com");
		// 创建一个验证器工厂
		BeanValidatorUtils.validateWithException(user);
		user.setEmail("123@sinacom");
		BeanValidatorUtils.validateWithException(user);
		System.out.println(user.getEmail());
	}

}
