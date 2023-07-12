package com.lind.common.core.parser.impl;

import com.lind.common.core.parser.FieldTypeParser;

public class NestedTypeParser extends BaseAbstractTypeParser implements FieldTypeParser<Object> {

	@Override
	public Object parser(Object source) {
		return source;
	}

}
