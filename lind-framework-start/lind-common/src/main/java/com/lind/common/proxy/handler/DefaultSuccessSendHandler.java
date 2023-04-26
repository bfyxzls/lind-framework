package com.lind.common.proxy.handler;

public class DefaultSuccessSendHandler implements SuccessSendHandler {

	@Override
	public void successSend(String message) {
		System.out.println("send " + message + " success");
	}

}
