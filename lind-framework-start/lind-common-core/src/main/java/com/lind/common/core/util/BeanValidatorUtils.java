package com.lind.common.core.util;

import javax.validation.*;
import java.util.Set;

/**
 * bean对象属性验证
 *
 * @author ruoyi
 */
public class BeanValidatorUtils {
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    public static void validateWithException(Object object, Class<?>... groups)
            throws ConstraintViolationException {
        Set<ConstraintViolation<Object>> constraintViolations = validatorFactory.getValidator().validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }

}
