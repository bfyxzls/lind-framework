package com.lind.common.core.parser.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lind.common.core.parser.FieldTypeParser;
import com.lind.common.core.parser.exception.FieldValidException;
import com.lind.common.core.parser.util.ParserUtils;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class DateTypeParser extends BaseAbstractTypeParser implements FieldTypeParser<Object> {

	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@SneakyThrows
	@Override
	public Object parser(Object source) {

		if (ObjectUtils.isEmpty(source)) {
			return null;
		}
		String trim = source.toString().trim();
		if (ParserUtils.isArray(trim)) {
			List<String> arr = getObjectMapper().readValue(trim, new TypeReference<List<String>>() {
			});
			List<LocalDateTime> result = new ArrayList<>();
			arr.forEach(o -> {
				result.add(convert(o));
			});
			return result;
		}
		return convert(trim);

	}

	private LocalDateTime convert(String source) {
		LocalDateTime parsedDateTime;
		String trim = source.trim();
		try { // 尝试使用 yyyy-MM-dd 格式解析为 LocalDate
			LocalDate parsedDate = LocalDate.parse(trim, dateFormatter);
			parsedDateTime = parsedDate.atStartOfDay();
		}
		catch (DateTimeParseException e) {
			try {
				parsedDateTime = LocalDateTime.parse(trim, dateTimeFormatter);
			}
			catch (DateTimeParseException err) {
				throw new FieldValidException(source, e);
			}
		}
		catch (Exception error) {
			throw new FieldValidException(source, error);
		}
		return parsedDateTime;
	}

}
