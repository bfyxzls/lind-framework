package com.lind.common.proxy.provider;

import com.lind.common.proxy.handler.MessageProviderHandler;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SmsMessageProviderHandler implements MessageProviderHandler {

	@Override
	public void send(String message) {
		System.out.println("sms send message:" + message);
	}

}
