package com.lind.common.pattern.strategy;

public class FlyAdvance implements Fly {

	/**
	 * 飞的执行.
	 */
	@Override
	public void doing(String name) {
		System.out.println(name + "飞的非常的快");
	}

}
