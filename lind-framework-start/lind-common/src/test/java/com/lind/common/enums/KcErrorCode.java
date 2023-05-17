package com.lind.common.enums;

/**
 * @author lind
 * @date 2023/5/10 17:02
 * @since 1.0.0
 */
public enum KcErrorCode {

	// 登录错误
	LOGIN_ERROR("U001", "登录错误"),
	// 用户密码错误
	USER_PASSWORD_ERROR("U002", "用户密码错误"),
	// 用户不存在或被禁用
	USER_NOT_FOUND("U003", "用户不存在或被禁用"),
	// UnionId不存在
	UNION_ID_NOT_FOUND("U004", "UnionId不存在"),
	// 用户被锁定
	USER_IS_LOCKED("U005", "用户被锁定"),
	// 手机号格式不正确
	PHONE_FORMAT_ERROR("U006", "手机号格式不正确"),
	// 手机号不存在
	PHONE_NOT_FOUND("U007", "手机号不存在"),
	// 微信跳板服务异常
	WECHAT_JUMP_ERROR("U008", "微信跳板服务异常"),
	// 微信跳板服务对应的产品类型不存在
	WECHAT_JUMP_PRODUCT_TYPE_NOT_FOUND("U009", "微信跳板服务对应的产品类型不存在");

	private String code;

	private String message;

	KcErrorCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public static String getMessage(KcErrorCode kcErrorCode) {
		for (KcErrorCode e : values()) {
			if (kcErrorCode.equals(e)) {
				return e.message;
			}
		}
		return null;
	}

}
