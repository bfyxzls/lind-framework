package com.lind.elasticsearch.parser;

public interface FieldTypeParser<T> {

	/**
	 * 解析字段
	 * @param source
	 * @return
	 */
	T parser(Object source);

}
