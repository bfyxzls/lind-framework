package com.lind.logback.trace;

import org.springframework.util.IdGenerator;

import java.util.UUID;

/**
 * @author lind
 * @date 2023/1/28 16:42
 * @since 1.0.0
 */
public class DefaultTraceGenerator implements IdGenerator {

	@Override
	public UUID generateId() {
		return UUID.randomUUID();
	}

}
