package com.lind.common.pattern.orderstatusmachine;

/**
 * @author lind
 * @date 2023/2/28 14:18
 * @since 1.0.0
 */
public class Order {

	private int id;

	private OrderStatus status;

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "订单号：" + id + ", 订单状态：" + status;
	}

}
