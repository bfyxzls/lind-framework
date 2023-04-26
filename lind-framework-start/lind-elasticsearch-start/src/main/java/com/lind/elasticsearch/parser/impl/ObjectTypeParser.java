package com.lind.elasticsearch.parser.impl;

import com.lind.elasticsearch.exception.FieldValidException;
import com.lind.elasticsearch.parser.FieldTypeParser;

public class ObjectTypeParser extends BaseAbstractTypeParser implements FieldTypeParser<Object> {

	@Override
	public Object parser(Object source) {

		try {
			return source;
		}
		catch (Exception e) {
			throw new FieldValidException(source, e);
		}

	}

}
