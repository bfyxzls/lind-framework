package com.lind.common.pattern.decorate2;

/**
 * 人类.
 */
public abstract class Human {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void run() {
		System.out.println("人类跑起来");
	}

}
