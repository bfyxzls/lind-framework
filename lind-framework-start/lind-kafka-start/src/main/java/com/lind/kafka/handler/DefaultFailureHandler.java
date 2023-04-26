package com.lind.kafka.handler;

import com.lind.kafka.entity.MessageEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.Map;

/**
 * 消息发送失败默认事件.
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultFailureHandler implements FailureHandler {

	@Override
	public void onFailure(String topic, MessageEntity messageEntity, Throwable ex, Map<String, String> mdcContextMap) {
		MDC.setContextMap(mdcContextMap);
		log.error("fail to send topic={}, data={}", topic, messageEntity, ex);
	}

}
