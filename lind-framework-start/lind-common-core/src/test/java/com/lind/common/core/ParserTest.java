package com.lind.common.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lind.common.core.parser.DefaultFieldTypeParserFactory;
import com.lind.common.core.parser.FieldTypeParser;
import com.lind.common.core.parser.FieldTypeParserFactory;
import com.lind.common.core.parser.impl.DateTypeParser;
import lombok.SneakyThrows;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.List;

public class ParserTest {

	static final FieldTypeParserFactory fieldTypeParserFactory = new DefaultFieldTypeParserFactory();
	static final FieldTypeParser fieldTypeParser = fieldTypeParserFactory.getParser(DateTypeParser.class);

	@Test
	public void parse() {
		fieldTypeParser.parser("2012-01-01");
		System.out.println(fieldTypeParser.parser("2012-01-01"));
	}

	@SneakyThrows
	@Test
	public void parseDateList() {
		List<String> dateArray = Lists.newArrayList("2023-01-02 00:00:00", "2023-02-02 00:00:00");
		String json = new ObjectMapper().writeValueAsString(dateArray);
		Object dateList = fieldTypeParser.parser(json);
		System.out.println(dateList);
	}

}
