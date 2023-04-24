package com.lind.redis.lock.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 防止重复提交.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface RepeatSubmit {

	/**
	 * 间隔多长时间提交,默认1秒.
	 */
	int expireTime() default 1;

	/**
	 * redis里存储的重复提交的key.
	 */
	String redisKey() default "submit-repeat";

}
