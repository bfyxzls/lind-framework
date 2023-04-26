package com.lind.redis.limit.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 限流注解.
 **/
@Target(ElementType.METHOD) // 作用于方法上
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {

	/**
	 * 并发5次.
	 * @return
	 */
	int limit() default 5;

	/**
	 * 1000毫秒.
	 * @return
	 */
	int timeout() default 1000;

}
