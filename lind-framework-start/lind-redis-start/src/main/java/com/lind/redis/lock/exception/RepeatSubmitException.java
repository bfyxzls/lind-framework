package com.lind.redis.lock.exception;

/**
 * 防治重复操作异常.
 */
public class RepeatSubmitException extends IllegalArgumentException {

	public RepeatSubmitException(String message) {
		super(message);
	}

}
