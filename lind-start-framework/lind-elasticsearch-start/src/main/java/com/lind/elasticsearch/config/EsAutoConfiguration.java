package com.lind.elasticsearch.config;

import com.lind.elasticsearch.parser.DefaultFieldTypeParserFactory;
import com.lind.elasticsearch.parser.FieldTypeParserFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EsAutoConfiguration {

	@Bean
	public FieldTypeParserFactory fieldTypeParserFactory() {
		return new DefaultFieldTypeParserFactory();
	}

}
