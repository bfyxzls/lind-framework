package com.lind.common.pattern.decorate;

/**
 * 糖的装饰器.
 */
public class SugarAbstractDrinkDecorate extends AbstractDrinkDecorate {

	public SugarAbstractDrinkDecorate(Drink drink) {
		super(drink);
		super.setName("糖");
		super.setPrice(3);
	}

}
