package com.lind.common.pattern.singleton;

/**
 * 饿汉模式.
 */
public class SingletonLazy {

	private SingletonLazy() {
	}

	public static final SingletonLazy getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private static class SingletonHolder {

		private static final SingletonLazy INSTANCE = new SingletonLazy();

	}

}
