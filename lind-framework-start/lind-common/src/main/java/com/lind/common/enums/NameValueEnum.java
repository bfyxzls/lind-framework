package com.lind.common.enums;

/**
 * 带有枚举值以及枚举名称的枚举接口(可使用{@link EnumUtils}中的方法)
 */
public interface NameValueEnum extends ValueEnum {

	/**
	 * 获取枚举名称
	 * @return 枚举名
	 */
	String getName();

}
