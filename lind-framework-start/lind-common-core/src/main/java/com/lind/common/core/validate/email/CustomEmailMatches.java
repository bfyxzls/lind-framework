package com.lind.common.core.validate.email;

import javax.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lind
 * @date 2023/7/20 16:35
 * @since 1.0.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CustomEmailMatchesValidator.class)
@Documented
public @interface CustomEmailMatches {

	String message() default "Invalid email address";

	Class<?>[] groups() default {};

	Class<? extends javax.validation.Payload>[] payload() default {};

}
