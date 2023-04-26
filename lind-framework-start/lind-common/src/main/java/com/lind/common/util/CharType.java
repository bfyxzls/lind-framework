package com.lind.common.util;

public interface CharType {

	/**
	 * 单字节
	 */
	byte CT_SINGLE = 5;

	/**
	 * 分隔符"!,.?()[]{}+=
	 */
	byte CT_DELIMITER = CT_SINGLE + 1;

	/**
	 * 中文字符
	 */
	byte CT_CHINESE = CT_SINGLE + 2;

	/**
	 * 字母
	 */
	byte CT_LETTER = CT_SINGLE + 3;

	/**
	 * 数字
	 */
	byte CT_NUM = CT_SINGLE + 4;

	/**
	 * 序号
	 */
	byte CT_INDEX = CT_SINGLE + 5;

	/**
	 * 中文数字
	 */
	byte CT_CNUM = CT_SINGLE + 6;

	/**
	 * 其他
	 */
	byte CT_OTHER = CT_SINGLE + 12;

	/**
	 * 条款项目
	 */
	byte LAW_TYPE = CT_SINGLE + 13;

	/**
	 * 需要略过的词（省市县）
	 */
	byte LAW_FUZZY = CT_SINGLE + 14;

	/**
	 * 左右括号
	 */
	byte LEFT_BRACKET = CT_SINGLE + 15;

	byte RIGHT_BRACKET = CT_SINGLE + 16;

}
