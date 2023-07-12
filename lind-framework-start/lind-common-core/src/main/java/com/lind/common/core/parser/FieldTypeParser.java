package com.lind.common.core.parser;

/**
 * 对从json字符串解析出来的数据进行格式化为java类型
 * @param <T>
 */
public interface FieldTypeParser<T> {

	/**
	 * 解析字段
	 * @param source
	 * @return
	 */
	T parser(Object source);

}
