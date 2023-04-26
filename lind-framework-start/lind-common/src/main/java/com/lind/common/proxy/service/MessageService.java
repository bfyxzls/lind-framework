package com.lind.common.proxy.service;

import com.lind.common.proxy.handler.MessageProviderHandler;
import com.lind.common.proxy.handler.SuccessSendHandler;

public interface MessageService<T> {

	/**
	 * 消息发送.
	 * @param message
	 * @param messageProviderHandler
	 * @param successSendHandler
	 */
	void send(T message, MessageProviderHandler messageProviderHandler, SuccessSendHandler successSendHandler);

}
