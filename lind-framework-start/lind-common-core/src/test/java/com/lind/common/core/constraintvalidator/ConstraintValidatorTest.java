package com.lind.common.core.constraintvalidator;

import com.lind.common.core.util.BeanValidatorUtils;
import org.junit.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

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
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = validatorFactory.getValidator();
		BeanValidatorUtils.validateWithException(validator, user);
		System.out.println(user.getEmail());
	}

}
