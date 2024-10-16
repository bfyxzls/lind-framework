package com.lind.fileupload.validator;

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
        return value.endsWith("_ROLE");
    }
}
