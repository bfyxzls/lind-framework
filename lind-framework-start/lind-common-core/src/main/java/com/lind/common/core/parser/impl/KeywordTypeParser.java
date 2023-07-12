package com.lind.common.core.parser.impl;

import com.lind.common.core.parser.FieldTypeParser;
import com.lind.common.core.parser.exception.FieldValidException;
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;
import java.util.Objects;

public class KeywordTypeParser extends BaseAbstractTypeParser implements FieldTypeParser<Object> {

	/**
	 * 最长的keyword长度
	 */
	private final int MAX_LENGTH = 5000;

	@Override
	public Object parser(Object source) {
		if (ObjectUtils.isEmpty(source)) {
			return null;
		}
		try {
			if (source instanceof List) {
				boolean b = ((List<?>) source).stream()
						.anyMatch(s -> Objects.nonNull(s) && s.toString().length() > MAX_LENGTH);
				if (b) {
					throw new FieldValidException(source, new IllegalArgumentException("keyword类型长度不可超过" + MAX_LENGTH));
				}
				return source;
			}
			String s = String.valueOf(source);
			if (s.length() > MAX_LENGTH) {
				throw new FieldValidException(source, new IllegalArgumentException("keyword类型长度不可超过" + MAX_LENGTH));
			}

			return s;
		}
		catch (Exception e) {
			throw new FieldValidException(source, e);
		}
	}

}
