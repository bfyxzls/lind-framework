package com.lind.elasticsearch.exception;

public class FieldValidException extends RuntimeException {

	private static final long serialVersionUID = -1925737465074574364L;

	private Object fieldName;

	private Throwable cause;

	public FieldValidException(String message) {
		super(message);
	}

	public FieldValidException(Object fieldName, Throwable cause) {
		this.fieldName = fieldName;
		this.cause = cause;
	}

	@Override
	public String getMessage() {
		String message = "数据转换异常：" + fieldName + ";" + cause.getMessage();
		return message;
	}

}
