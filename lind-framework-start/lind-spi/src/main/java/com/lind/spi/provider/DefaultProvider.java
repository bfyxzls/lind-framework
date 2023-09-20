package com.lind.spi.provider;

public class DefaultProvider implements Provider {

	@Override
	public String login() {
		return "DefaultHelloProvider登录";
	}

}
