package com.lind.rbac.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PrefixAdminValidator.class)
@Documented
public @interface PrefixAdmin {

	String message() default "必须以'角色'结尾";

	// 这两个属性必须有
	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
