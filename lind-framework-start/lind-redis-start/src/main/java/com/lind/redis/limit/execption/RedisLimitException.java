package com.lind.redis.limit.execption;

/**
 * 限流操作异常.
 */
public class RedisLimitException extends RuntimeException {

	public RedisLimitException(String message) {
		super(message);
	}

}
