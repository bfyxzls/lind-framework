package com.lind.elasticsearch;

import com.lind.elasticsearch.parser.DefaultFieldTypeParserFactory;
import com.lind.elasticsearch.parser.FieldTypeParser;
import com.lind.elasticsearch.parser.FieldTypeParserFactory;
import com.lind.elasticsearch.parser.impl.DateTypeParser;
import org.junit.Test;

public class ParserTest {

	@Test
	public void parse() {
		FieldTypeParserFactory fieldTypeParserFactory = new DefaultFieldTypeParserFactory();
		FieldTypeParser fieldTypeParser = fieldTypeParserFactory.getParser(DateTypeParser.class);
		fieldTypeParser.parser("2012-01-01");
		System.out.println(fieldTypeParser.parser("2012-01-01"));
	}

}
