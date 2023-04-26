package com.lind.uaa.jwt.anno;

import com.lind.uaa.jwt.permission.Logical;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限认证：必须具有指定权限才能进入该方法
 *
 * @author ruoyi
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface RequiresPermissions {

	/**
	 * 需要校验的权限码
	 */
	String[] value() default {};

	Logical logical() default Logical.AND;

}
