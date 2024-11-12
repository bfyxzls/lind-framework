package com.lind.hibernate.validator;

import jdk.nashorn.internal.ir.annotations.Ignore;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author lind
 * @date 2024/10/15 16:11
 * @since 1.0.0
 */
public class PrefixRoleMatchValidator implements ConstraintValidator<PrefixRoleMatch, String> {

	@Override
	public void initialize(PrefixRoleMatch constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		// 根据分组条件进行验证
		if (context.getDefaultConstraintMessageTemplate().contains(Ignore.class.getName())) {
			return true;
		}
		return value != null && value.endsWith("_ROLE");
	}

}
