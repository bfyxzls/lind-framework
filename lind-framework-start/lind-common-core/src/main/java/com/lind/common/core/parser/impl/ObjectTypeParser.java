package com.lind.common.core.parser.impl;

import com.lind.common.core.parser.FieldTypeParser;
import com.lind.common.core.parser.exception.FieldValidException;

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
