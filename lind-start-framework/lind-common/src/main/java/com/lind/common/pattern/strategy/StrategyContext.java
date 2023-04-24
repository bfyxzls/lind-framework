package com.lind.common.pattern.strategy;

/**
 * 策略上下文-策略的持有者.
 */
public class StrategyContext {

	private Animal animal;

	public StrategyContext(Animal animal) {
		this.animal = animal;
	}

	public void fly() {
		animal.getFly().doing(animal.getName());
	}

}
