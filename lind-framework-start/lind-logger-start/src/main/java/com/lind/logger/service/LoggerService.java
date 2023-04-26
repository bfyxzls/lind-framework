package com.lind.logger.service;

import com.lind.logger.entity.LoggerInfo;

/**
 * 日志持久化接口.
 */
public interface LoggerService {

	void insert(LoggerInfo loggerInfo);

}
