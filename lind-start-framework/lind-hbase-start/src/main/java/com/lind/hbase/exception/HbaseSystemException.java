package com.lind.hbase.exception;

/**
 * @author BD-PC220
 */
public class HbaseSystemException extends RuntimeException {

	public HbaseSystemException(Exception cause) {
		super(cause.getMessage(), cause);
	}

	public HbaseSystemException(Throwable throwable) {
		super(throwable.getMessage(), throwable);
	}

}
