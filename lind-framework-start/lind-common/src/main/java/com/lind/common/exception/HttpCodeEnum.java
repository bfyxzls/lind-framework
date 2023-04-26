package com.lind.common.exception;

/**
 * 返回状态码.
 *
 * @author BD-PC220
 */
public enum HttpCodeEnum {

	/**
	 * 成功.
	 */
	SUCCESS(200, "success"),

	/**
	 * 失败.
	 */
	FAILURE(-1, "failure"),

	/**
	 * 系统内部错误.
	 */
	INTERNAL_SERVER_ERROR(500, "系统错误"),

	/**
	 * 客户端错误.
	 */
	ILLEGAL_ARGUMENT(400, "客户端错误"),

	/**
	 * 没有授权.
	 */
	UNAUTHORIZED(401, "没有授权"),

	/**
	 * 权限不足.
	 */
	FORBIDDEN(403, "权限不足");

	private final Integer code;

	private final String message;

	HttpCodeEnum(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public Integer getCode() {
		return this.code;
	}

	public String getMessage() {
		return this.message;
	}

}
