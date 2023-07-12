package com.lind.common.core.parser.impl;

import com.lind.common.core.parser.FieldTypeParser;
import com.lind.common.core.parser.exception.FieldValidException;
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;

public class TextTypeParser extends BaseAbstractTypeParser implements FieldTypeParser<Object> {

	@Override
	public Object parser(Object source) {
		if (ObjectUtils.isEmpty(source)) {
			return null;
		}
		try {
			if (source instanceof List) {
				return source;
			}
			// String s = source.toString().trim();
			// if (ParserUtils.isArray(s)) {
			// List<String> strings = getObjectMapper().readValue(s, new
			// TypeReference<List<String>>() {
			// });
			// return strings;
			// }
			return String.valueOf(source);
		}
		catch (Exception e) {
			throw new FieldValidException(source, e);
		}
	}

}
