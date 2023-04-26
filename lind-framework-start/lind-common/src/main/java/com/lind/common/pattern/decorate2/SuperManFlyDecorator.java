package com.lind.common.pattern.decorate2;

/**
 * 超人的飞机装饰器.
 */
public class SuperManFlyDecorator extends FlyDecorator {

	public SuperManFlyDecorator(Human human) {
		super(human);
	}

	@Override
	protected void fly() {
		System.out.println("超人会飞");
	}

}
