package com.lind.common.proxy.service;

import com.lind.common.proxy.handler.MessageProviderHandler;
import com.lind.common.proxy.handler.SuccessSendHandler;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultMessageService implements MessageService<String> {

	@Override
	public void send(String message, MessageProviderHandler messageProviderHandler,
			SuccessSendHandler successSendHandler) {
		messageProviderHandler.send(message);
		successSendHandler.successSend(message);
	}

}
