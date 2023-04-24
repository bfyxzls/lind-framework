package com.lind.common.pattern.decorate;

public class Tea extends Drink {

	public Tea() {
		super.setName("茶");
		super.setPrice(4);
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
