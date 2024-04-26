package com.lind.redis.limit.execption;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author lind
 * @date 2024/4/26 17:28
 * @since 1.0.0
 */
@ControllerAdvice
@RestControllerAdvice
public class RedisGlobalExceptionHandler {

	@ExceptionHandler({ RedisLimitException.class })
	@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
	public ResponseEntity handleException(RedisLimitException e) {
		String message = "TOO_MANY_REQUESTS";
		return ResponseEntity.status(429).body(message);
	}

}
