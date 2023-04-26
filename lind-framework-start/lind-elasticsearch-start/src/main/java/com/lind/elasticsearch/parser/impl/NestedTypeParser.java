package com.lind.elasticsearch.parser.impl;

import com.lind.elasticsearch.parser.FieldTypeParser;

public class NestedTypeParser extends BaseAbstractTypeParser implements FieldTypeParser<Object> {

	@Override
	public Object parser(Object source) {
		return source;
	}

}
