package com.lind.verification.code.common;

/**
 * The interface Security constants.
 *
 * @author paascloud.net@gmail.com
 */
public interface CodeConstants {

	/**
	 * 默认的处理验证码的url前缀
	 */
	String DEFAULT_VALIDATE_CODE_URL_PREFIX = "/auth/code";

	/**
	 * 检查验证码
	 */
	String DEFAULT_CHECK_CODE_URL_PREFIX = "/auth/check";

	/**
	 * 验证图片验证码时，http请求中默认的携带图片验证码信息的参数的名称
	 */
	String DEFAULT_PARAMETER_NAME_CODE_IMAGE = "image";

	/**
	 * 验证图片验证码直接响应为图像流.
	 */
	String DEFAULT_PARAMETER_NAME_CODE_IMAGE_STREAM = "imagestream";

	/**
	 * 验证短信验证码时，http请求中默认的携带短信验证码信息的参数的名称
	 */
	String DEFAULT_PARAMETER_NAME_CODE_SMS = "sms";

	/**
	 * 验证邮箱验证码时，http请求中默认的携带短信验证码信息的参数的名称
	 */
	String DEFAULT_PARAMETER_NAME_CODE_EMAIL = "email";

	/**
	 * 发送短信验证码 或 验证短信验证码时，传递手机号的参数的名称
	 */
	String DEFAULT_PARAMETER_NAME_MOBILE = "mobile";

	/**
	 * 发送邮箱验证码 或 验证邮箱验证码时，传递邮箱的参数的名称
	 */
	String DEFAULT_PARAMETER_NAME_EMAIL = "email";

}
