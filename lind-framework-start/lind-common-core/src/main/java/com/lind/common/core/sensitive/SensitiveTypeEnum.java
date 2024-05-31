package com.lind.common.core.sensitive;

/**
 * des: 敏感信息枚举类
 */
public enum SensitiveTypeEnum {

	/**
	 * 自定义
	 */
	CUSTOMER,
	/**
	 * 身份证号, 110110********1234
	 */
	ID_CARD,
	/**
	 * 座机号, ****1234
	 */
	FIXED_PHONE,
	/**
	 * 手机号, 176****1234
	 */
	MOBILE_PHONE,
	/**
	 * 地址, 北京********
	 */
	ADDRESS,
	/**
	 * 电子邮件, s*****o@xx.com
	 */
	EMAIL,
	/**
	 * 银行卡, 622202************1234
	 */
	BANK_CARD,
	/**
	 * 密码, 永远是 ******, 与长度无关
	 */
	PASSWORD,
	/**
	 * 密钥, 永远是 ******, 与长度无关
	 */
	KEY,
	/**
	 * 用户名, 张*香, 张*
	 */
	CHINESE_NAME

}
