package com.lind.common.core.parser.impl;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class BaseAbstractTypeParser {

	public ObjectMapper getObjectMapper() {
		return new ObjectMapper();
	}

}
