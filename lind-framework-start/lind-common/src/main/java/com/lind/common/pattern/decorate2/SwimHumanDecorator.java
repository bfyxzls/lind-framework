package com.lind.common.pattern.decorate2;

/**
 * @author lind
 * @date 2024/4/23 11:14
 * @since 1.0.0
 */
public class SwimHumanDecorator extends Human {

	private Human human;

	public SwimHumanDecorator(Human human) {
		this.human = human;
	}

	@Override
	public void run() {
		if (human != null) {
			human.run();
		}
		swim();
	}

	private void swim() {
		System.out.println("会游泳");
	}

}
