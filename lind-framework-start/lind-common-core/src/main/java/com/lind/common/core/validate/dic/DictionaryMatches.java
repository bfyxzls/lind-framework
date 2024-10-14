package com.lind.common.core.validate.dic;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 用于验证字典值是否在指定范围内的注解 Created by macro on 2018/4/26.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Constraint(validatedBy = DictionaryMatchesValidator.class)
public @interface DictionaryMatches {

	String[] value() default {};

	String message() default "flag is not found";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
