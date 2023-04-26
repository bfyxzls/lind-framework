package com.lind.common.exception;

import lombok.Getter;

/**
 * 框架异常.
 */
@Getter
public class LindException extends RuntimeException {

	private final String msg;

	private Throwable throwable;

	public LindException(String msg) {
		super(msg);
		this.msg = msg;
	}

	/**
	 * init.
	 */
	public LindException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.throwable = e;
	}

}
