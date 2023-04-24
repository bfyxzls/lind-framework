package com.lind.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lind.kafka.entity.MessageEntity;
import com.lind.kafka.handler.FailureHandler;
import com.lind.kafka.handler.SuccessHandler;

/**
 * 发送者.
 **/
public interface MessageSender<T extends MessageEntity> {

	void send(String topic, T message) throws JsonProcessingException;

	void send(String topic, T message, SuccessHandler successHandler) throws JsonProcessingException;

	void send(String topic, T message, FailureHandler failureHandler) throws JsonProcessingException;

	void send(String topic, T message, SuccessHandler successHandler, FailureHandler failureHandler)
			throws JsonProcessingException;

}
