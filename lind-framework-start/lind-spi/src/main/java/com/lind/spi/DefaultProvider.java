package com.lind.spi;

public class DefaultProvider implements Provider {

	@Override
	public String login() {
		return "DefaultHelloProvider登录";
	}

}
