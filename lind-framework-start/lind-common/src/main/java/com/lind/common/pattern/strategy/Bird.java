package com.lind.common.pattern.strategy;

public class Bird extends Animal {

	public Bird() {
		super(new FlySlow(), "小鸟");
	}

}
