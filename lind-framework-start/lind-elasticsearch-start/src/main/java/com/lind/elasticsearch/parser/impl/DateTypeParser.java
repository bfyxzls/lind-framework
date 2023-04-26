package com.lind.elasticsearch.parser.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.lind.elasticsearch.exception.FieldValidException;
import com.lind.elasticsearch.parser.FieldTypeParser;
import com.lind.elasticsearch.util.ParserUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Date;
import java.util.List;

public class DateTypeParser extends BaseAbstractTypeParser implements FieldTypeParser<Object> {

	@Override
	public Object parser(Object source) {

		if (ObjectUtils.isEmpty(source)) {
			return null;
		}

		try {
			String trim = source.toString().trim();
			if (ParserUtils.isArray(trim)) {
				return getObjectMapper().readValue(trim, new TypeReference<List<Date>>() {
				});
			}
			DateTime parse = DateUtil.parse(source.toString());
			long time = parse.getTime();
			return new Date(time);
		}
		catch (Exception e) {
			throw new FieldValidException(source, e);
		}
	}

}
