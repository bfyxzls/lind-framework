package com.lind.common.proxy.provider;

import com.lind.common.proxy.handler.MessageProviderHandler;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailMessageProviderHandler implements MessageProviderHandler {

	@Override
	public void send(String message) {
		System.out.println("email send message:" + message);
	}

}
