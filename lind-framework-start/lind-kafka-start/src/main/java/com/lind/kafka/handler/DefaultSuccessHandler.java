package com.lind.kafka.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lind.kafka.entity.MessageEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.MDC;
import org.springframework.kafka.support.SendResult;

import java.util.Map;

/**
 * 消息发送成功默认事件.
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultSuccessHandler implements SuccessHandler {

	private final ObjectMapper objectMapper;

	@Override
	public void onSuccess(SendResult<String, String> result, Map<String, String> mdcContextMap) {
		MDC.setContextMap(mdcContextMap);
		String topic = result.getProducerRecord().topic();
		try {
			MessageEntity value = objectMapper.readValue(result.getProducerRecord().value(), MessageEntity.class);

			RecordMetadata recordMetadata = result.getRecordMetadata();
			int partition = recordMetadata.partition();
			long offset = recordMetadata.offset();

			log.info("success sent topic={} data={} with offset={} partition={}", topic, value, offset, partition);
		}
		catch (JsonProcessingException e) {
			log.error("json反序列化失败", e);
		}

	}

}
