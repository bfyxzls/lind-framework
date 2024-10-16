package com.lind.fileupload.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PrefixRoleMatchValidator.class)
@Documented
public @interface PrefixRoleMatch {
    String message() default "必须以'_ROLE'结尾";
    //这两个属性必须有
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
