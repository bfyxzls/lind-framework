package com.lind.common.aspect.repeat;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TryDo {

	/**
	 * 频率
	 */
	int frequency() default 1000;

	/**
	 * 次数
	 */
	int limit() default 3;

	/**
	 * 打印日志的名称
	 * @return
	 */
	@AliasFor("name")
	String value() default "";

	@AliasFor("value")
	String name() default "";

}
