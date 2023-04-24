package com.lind.spi;

public class DefaultProviderFactory implements ProviderFactory<DefaultProvider> {

	@Override
	public DefaultProvider create() {
		return new DefaultProvider();
	}

	@Override
	public String getId() {
		return "DefaultHelloProvider";
	}

}
