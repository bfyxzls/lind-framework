package com.lind.redis.annotation;

import java.lang.annotation.*;

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
