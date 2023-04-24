package com.lind.common.pattern.decorate;

/**
 * 饮料-茶.
 */
public class Coffee extends Drink {

	public Coffee() {
		super.setName("咖啡");
		super.setPrice(7);
	}

	/**
	 * 计算价格.
	 * @return
	 */
	@Override
	public double cost() {
		return this.getPrice();
	}

}
