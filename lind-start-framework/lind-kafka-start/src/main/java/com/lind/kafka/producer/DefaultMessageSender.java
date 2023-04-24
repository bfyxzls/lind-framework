package com.lind.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lind.kafka.entity.MessageEntity;
import com.lind.kafka.handler.FailureHandler;
import com.lind.kafka.handler.SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.slf4j.MDC;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.UUID;

/**
 * 默认消息发送者.
 **/
@RequiredArgsConstructor
public class DefaultMessageSender implements MessageSender<MessageEntity> {

	private final SuccessHandler successHandler;

	private final FailureHandler failureHandler;

	private final KafkaTemplate<String, String> template;

	private final ObjectMapper objectMapper;

	@Override
	public void send(String topic, MessageEntity message) throws JsonProcessingException {
		sendMessage(topic, message, successHandler, failureHandler);
	}

	@Override
	public void send(String topic, MessageEntity message, SuccessHandler successHandler)
			throws JsonProcessingException {
		sendMessage(topic, message, successHandler, failureHandler);
	}

	@Override
	public void send(String topic, MessageEntity message, FailureHandler failureHandler)
			throws JsonProcessingException {
		sendMessage(topic, message, successHandler, failureHandler);
	}

	@Override
	public void send(String topic, MessageEntity message, SuccessHandler successHandler, FailureHandler failureHandler)
			throws JsonProcessingException {
		sendMessage(topic, message, successHandler, failureHandler);
	}

	/**
	 * 发送消息到kafka
	 * @param topic
	 * @param message
	 * @param successHandler
	 * @param failureHandler
	 */
	private void sendMessage(String topic, MessageEntity message, SuccessHandler successHandler,
			FailureHandler failureHandler) throws JsonProcessingException {
		String s = objectMapper.writeValueAsString(message);
		RecordHeaders headers = new RecordHeaders();
		if (MDC.getCopyOfContextMap() == null || !MDC.getCopyOfContextMap().containsKey("traceId")) {
			MDC.put("traceId", UUID.randomUUID().toString().replace("-", ""));
		}
		headers.add("traceId", MDC.get("traceId").getBytes());
		ProducerRecord<String, String> producerRecord = new ProducerRecord(topic, null, null, null, s, headers);

		ListenableFuture<SendResult<String, String>> send = template.send(producerRecord);
		send.addCallback(new ListenableFutureCallbackWithTracing<>(MDC.getCopyOfContextMap(), successHandler,
				failureHandler, topic, message));

	}

}
