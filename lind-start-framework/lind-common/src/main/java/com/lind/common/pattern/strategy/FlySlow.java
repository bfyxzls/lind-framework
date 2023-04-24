package com.lind.common.pattern.strategy;

public class FlySlow implements Fly {

	/**
	 * 飞的执行.
	 */
	@Override
	public void doing(String name) {
		System.out.println(name + "飞的很慢");
	}

}
