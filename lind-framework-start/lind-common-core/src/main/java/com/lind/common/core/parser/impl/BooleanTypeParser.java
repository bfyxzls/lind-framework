package com.lind.common.core.parser.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lind.common.core.parser.FieldTypeParser;
import com.lind.common.core.parser.exception.FieldValidException;
import com.lind.common.core.parser.util.ParserUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;

public class BooleanTypeParser extends BaseAbstractTypeParser implements FieldTypeParser<Object> {

	@Override
	public Object parser(Object source) {
		if (ObjectUtils.isEmpty(source)) {
			return null;
		}
		try {
			String s = source.toString().trim();
			if (ParserUtils.isArray(s)) {
				List<Boolean> booleans = getObjectMapper().readValue(s, new TypeReference<List<Boolean>>() {
				});
				return booleans;
			}
			return Boolean.parseBoolean(source.toString());
		}
		catch (Exception e) {
			throw new FieldValidException(source, e);
		}
	}

}
