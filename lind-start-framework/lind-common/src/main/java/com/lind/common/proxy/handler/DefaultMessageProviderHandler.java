package com.lind.common.proxy.handler;

public class DefaultMessageProviderHandler implements MessageProviderHandler {

	@Override
	public void send(String message) {
		System.out.println("console send message:" + message);
	}

}
