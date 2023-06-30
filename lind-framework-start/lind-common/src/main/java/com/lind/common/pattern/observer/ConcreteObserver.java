package com.lind.common.pattern.observer;

/**
 * @author lind
 * @date 2023/6/27 15:06
 * @since 1.0.0
 */
public class ConcreteObserver implements Observer {

	private String name;

	public ConcreteObserver(String name) {
		this.name = name;
	}

	@Override
	public void update(String message) {
		System.out.println(name + " received message: " + message);
	}

}
