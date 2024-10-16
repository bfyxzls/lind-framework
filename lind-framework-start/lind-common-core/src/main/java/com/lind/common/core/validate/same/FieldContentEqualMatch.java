package com.lind.common.core.validate.same;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author lind
 * @date 2024/10/14 11:29
 * @since 1.0.0
 */
@Constraint(validatedBy = FieldContentEqualMatchValidator.class)
@Target({ TYPE })
@Retention(RUNTIME)
public @interface FieldContentEqualMatch {

	String message() default "内容不一致";

	/**
	 * 校验组验证时，需要为它指定一个校验组
	 * @return
	 */
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

	@Target({ TYPE, ANNOTATION_TYPE })
	@Retention(RUNTIME)
	@Documented
	@interface List {

		FieldContentEqualMatch[] value();

	}

}
