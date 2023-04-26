package com.lind.elasticsearch.parser.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lind.elasticsearch.util.JacksonUtils;

public abstract class BaseAbstractTypeParser {

	public ObjectMapper getObjectMapper() {
		return JacksonUtils.getObjectMapper();
	}

}
