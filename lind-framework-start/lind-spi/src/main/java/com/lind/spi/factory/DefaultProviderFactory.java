package com.lind.spi.factory;

import com.lind.spi.provider.DefaultProvider;

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
