package com.lind.rbac.valid;

import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class PrefixAdminValidator implements ConstraintValidator<PrefixAdmin, String> {

	@Override
	public void initialize(PrefixAdmin constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return value.endsWith("角色");
	}

}
