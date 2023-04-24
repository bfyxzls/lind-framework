package com.lind.common.pattern.decorate;

/**
 * 饮料基类，被装饰的对象基类，装饰器需要继承它.
 */
public abstract class Drink {

	private String name;

	private double price;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String printer() {
		return this.name + "价格:" + this.getPrice();
	}

	/**
	 * 计算价格.
	 * @return
	 */
	public abstract double cost();

}
