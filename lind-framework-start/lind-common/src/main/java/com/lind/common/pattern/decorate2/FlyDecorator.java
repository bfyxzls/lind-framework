package com.lind.common.pattern.decorate2;

/**
 * 飞行装饰器.
 */
public abstract class FlyDecorator extends Human {

	protected abstract void fly();

	private Human human;

	public FlyDecorator(Human human) {
		this.human = human;
	}

	@Override
	public void run() {
		if (human != null) {
			human.run();
		}
		fly();
	}

}
