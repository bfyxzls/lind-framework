package com.lind.common.pattern.decorate;

/**
 * 饮料装饰器基类,所有装饰饮料的decorate都需要继承它.
 */
public abstract class AbstractDrinkDecorate extends Drink {

	private Drink drink;

	public AbstractDrinkDecorate(Drink drink) {
		this.drink = drink;
	}

	@Override
	public String printer() {
		return String.format("名称:%s,价格:%s,%s", super.getName(), super.getPrice(), drink.printer());
	}

	/**
	 * 计算原品和所有装饰器的价格.
	 * @return
	 */
	@Override
	public double cost() {
		// 装饰自己的价格到原有的对象上
		return super.getPrice() + drink.cost();
	}

}
