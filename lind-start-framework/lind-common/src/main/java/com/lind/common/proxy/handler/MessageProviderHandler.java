package com.lind.common.proxy.handler;

/**
 * 消息发送的提供者，如短信、微信、邮件等.
 */
@FunctionalInterface
public interface MessageProviderHandler {

	void send(String message);

}
