package com.lind.uaa.util;

import lombok.Getter;

/**
 * uaa异常.
 */
@Getter
public class UAAException extends RuntimeException {

	private String msg;

	private Throwable e;

	public UAAException(String msg) {
		super(msg);
		this.msg = msg;
	}

	public UAAException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.e = e;
	}

}
