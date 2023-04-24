package com.lind.common.pattern.decorate;

/**
 * 牛奶的装饰器.
 */
public class MilkAbstractDrinkDecorate extends AbstractDrinkDecorate {

	public MilkAbstractDrinkDecorate(Drink drink) {
		super(drink);
		super.setName("牛奶");
		super.setPrice(5);
	}

}
