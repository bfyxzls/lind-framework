package com.lind.common.core.validate.flag;

import com.lind.common.core.validate.dic.DicValidator;

import javax.validation.Constraint;
import java.lang.annotation.*;

/**
 * 二进制标识约束校验器，保证集合中的值为2的N次幂.
 * @author lind
 * @date 2024/2/20 9:11
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Constraint(validatedBy = FlagValidator.class)
public @interface ValidFlag {

	String message() default "Invalid number,this value must 2^N";

	Class<?>[] groups() default {};

	Class<? extends javax.validation.Payload>[] payload() default {};

}
