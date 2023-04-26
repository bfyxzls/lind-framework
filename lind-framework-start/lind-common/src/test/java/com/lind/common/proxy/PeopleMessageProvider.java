package com.lind.common.proxy;

import com.lind.common.proxy.anno.MessageProvider;
import com.lind.common.proxy.anno.MessageSend;
import com.lind.common.proxy.provider.SmsMessageProviderHandler;

@MessageProvider
public interface PeopleMessageProvider {

	@MessageSend(messageProviderHandler = SmsMessageProviderHandler.class)
	void send(String message);

}