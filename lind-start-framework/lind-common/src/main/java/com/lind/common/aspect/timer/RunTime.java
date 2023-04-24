package com.lind.common.aspect.timer;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RunTime {

	/**
	 * 打印日志的名称
	 * @return
	 */
	@AliasFor("name")
	String value() default "";

	@AliasFor("value")
	String name() default "";

}
