package com.lind.kafka.handler;

import org.springframework.kafka.support.SendResult;

import java.util.Map;

/**
 * 发送成功的回调函数
 *
 * @author BD-PC220
 */
@FunctionalInterface
public interface SuccessHandler {

	/**
	 * 发送成功
	 * @param result kafka返回结果
	 */
	void onSuccess(SendResult<String, String> result, Map<String, String> mdcContextMap);

}
