package com.lind.common.pattern.strategy;

/**
 * 动物基类.
 */
public abstract class Animal {

	private Fly fly;

	private String name;

	public Animal(Fly fly, String name) {
		this.fly = fly;
		this.name = name;
	}

	public Fly getFly() {
		return fly;
	}

	public String getName() {
		return name;
	}

}
