package com.lind.common.exception;

import lombok.Getter;

/**
 * 框架异常.
 */
@Getter
public class LindException extends RuntimeException {

	private final String message;

	private String code;

	private Throwable throwable;

	public LindException(String message) {
		super(message);
		this.message = message;
	}

	public LindException(String message, String code) {
		super(message);
		this.message = message;
		this.code = code;
	}

	public LindException(String message, Throwable cause, String code, Throwable throwable) {
		super(message, cause);
		this.message = message;
		this.code = code;
		this.throwable = throwable;
	}

}
