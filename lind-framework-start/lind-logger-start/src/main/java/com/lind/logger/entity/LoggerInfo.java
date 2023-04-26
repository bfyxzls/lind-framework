package com.lind.logger.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LoggerInfo {

	/**
	 * 被记录的模块
	 */
	private Integer moduleType;

	/**
	 * 被记录的操作
	 */
	private Integer operateType;

	/**
	 * 日志数据ID.
	 */
	private String dataId;

	/**
	 * 日志数据标题.
	 */
	private String dataTitle;

	/**
	 * 操作人
	 */
	private String operator;

	/**
	 * 操作时间
	 */
	private LocalDateTime operateTime;

	/**
	 * 操作人IP.
	 */
	private String operatorIp;

	/**
	 * 内容
	 */
	private String detail;

}
