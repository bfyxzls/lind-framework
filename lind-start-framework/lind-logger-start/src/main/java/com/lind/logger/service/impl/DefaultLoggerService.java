package com.lind.logger.service.impl;

import com.lind.logger.entity.LoggerInfo;
import com.lind.logger.service.LoggerService;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认实现方式.
 */
@Slf4j
public class DefaultLoggerService implements LoggerService {

	@Override
	public void insert(LoggerInfo loggerInfo) {
		log.info("enter logger:{}", loggerInfo);
	}

}
