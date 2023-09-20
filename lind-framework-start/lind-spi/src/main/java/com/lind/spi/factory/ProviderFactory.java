package com.lind.spi.factory;

import com.lind.spi.provider.Provider;

public interface ProviderFactory<I extends Provider> {

	/**
	 * 建立Provider对象.
	 * @return
	 */
	I create();

	/**
	 * Provider对象ID.
	 * @return
	 */
	String getId();

}
