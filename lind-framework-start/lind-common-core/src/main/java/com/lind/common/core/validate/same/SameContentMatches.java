package com.lind.common.core.validate.same;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Function;

/**
 * @author lind
 * @date 2024/10/14 11:29
 * @since 1.0.0
 */
@Constraint(validatedBy = SameContentMatchesValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface SameContentMatches {

	String message() default "内容不一致";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * 源字段名
	 * @return
	 */
	String sourceField();

	/**
	 * 目标字段名
	 * @return
	 */
	String destinationField();
}
