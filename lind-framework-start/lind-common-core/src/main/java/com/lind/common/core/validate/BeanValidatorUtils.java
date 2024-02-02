package com.lind.common.core.validate;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * bean对象属性验证
 * 使用此方法等同于参数前加注解@Validated
 *
 * @author ruoyi
 */
public class BeanValidatorUtils {

	private static final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

	public static void validateWithException(Object object, Class<?>... groups) throws ConstraintViolationException {
		Set<ConstraintViolation<Object>> constraintViolations = validatorFactory.getValidator().validate(object,
				groups);
		if (!constraintViolations.isEmpty()) {
			throw new ConstraintViolationException(constraintViolations);
		}
	}

}
