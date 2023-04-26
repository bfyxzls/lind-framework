package com.lind.spi;

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
