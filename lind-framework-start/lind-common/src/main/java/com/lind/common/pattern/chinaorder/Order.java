package com.lind.common.pattern.chinaorder;

/**
 * @author lind
 * @date 2023/6/27 16:14
 * @since 1.0.0
 */
public class Order {

	private final String orderId;

	private double totalPrice;

	/**
	 * 是否可以叠加打折
	 */
	private Boolean isAccumulated;

	public Order(String orderId, double totalPrice, Boolean isAccumulated) {
		this.orderId = orderId;
		this.totalPrice = totalPrice;
		this.isAccumulated = isAccumulated;
	}

	public Boolean getAccumulated() {
		return isAccumulated;
	}

	public String getOrderId() {
		return orderId;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

}
