package com.lind.hbase.exception;

/**
 * hbase统一异常.
 */
public class HbaseSystemException extends RuntimeException {

	public HbaseSystemException(Exception cause) {
		super(cause.getMessage(), cause);
	}

	public HbaseSystemException(Throwable throwable) {
		super(throwable.getMessage(), throwable);
	}

}
