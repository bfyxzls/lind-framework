package com.lind.common.pattern.strategy;

public class Eagle extends Animal {

	public Eagle() {
		super(new FlyAdvance(), "鹰");
	}

}
