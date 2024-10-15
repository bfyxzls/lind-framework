package com.lind.common.core.validate.phone;

import javax.validation.Constraint;
import java.lang.annotation.*;

/**
 * @author lind
 * @date 2023/7/20 16:35
 * @since 1.0.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CustomPhoneMatchValidator.class)
@Documented
public @interface CustomPhoneMatch {

	String message() default "Invalid phone number";

	Class<?>[] groups() default {};

	Class<? extends javax.validation.Payload>[] payload() default {};

}
