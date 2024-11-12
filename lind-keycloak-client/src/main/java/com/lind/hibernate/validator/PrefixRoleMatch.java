package com.lind.hibernate.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 我如何实现这个注解在执行前，先判断groups的状态
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PrefixRoleMatchValidator.class)
@Documented
public @interface PrefixRoleMatch {

	String message() default "必须以'_ROLE'结尾";

	// 这两个属性必须有
	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * 可忽略的规则
	 */
	interface Ignore {

	}

}
