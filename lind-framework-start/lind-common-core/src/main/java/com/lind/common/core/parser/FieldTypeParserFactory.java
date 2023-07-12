package com.lind.common.core.parser;

public interface FieldTypeParserFactory {

	/**
	 * 获取解析器
	 * @param parserClazzName
	 * @return
	 */
	FieldTypeParser getParser(Class<? extends FieldTypeParser> parserClazzName);

}
