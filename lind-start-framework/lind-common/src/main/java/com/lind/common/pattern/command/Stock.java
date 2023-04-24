package com.lind.common.pattern.command;

/**
 * 请求类。
 */
public class Stock {

	private String name = "ABC";

	private int quantity = 10;

	public void buy() {
		System.out.println("Stock [ Name: " + name + ",Quantity: " + quantity + " ] 买入");
	}

	public void sell() {
		System.out.println("Stock [ Name: " + name + ",Quantity: " + quantity + " ] 卖出");
	}

	public void clear() {
		System.out.println("Stock [ Name: " + name + "] 清仓");
	}

}
