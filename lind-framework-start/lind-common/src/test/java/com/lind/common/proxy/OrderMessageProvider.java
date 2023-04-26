package com.lind.common.proxy;

import com.lind.common.proxy.anno.MessageProvider;
import com.lind.common.proxy.anno.MessageSend;
import com.lind.common.proxy.provider.EmailMessageProviderHandler;

@MessageProvider
public interface OrderMessageProvider {

	@MessageSend(messageProviderHandler = EmailMessageProviderHandler.class)
	void send(String message);

}