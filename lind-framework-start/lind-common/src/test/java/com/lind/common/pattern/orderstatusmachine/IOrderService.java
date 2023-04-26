package com.lind.common.pattern.orderstatusmachine;

import java.util.Map;

/**
 * @author lind
 * @date 2023/2/28 14:20
 * @since 1.0.0
 */
public interface IOrderService {

	// 创建新订单
	Order create();

	// 发起支付
	Order pay(int id);

	// 订单发货
	Order deliver(int id);

	// 订单收货
	Order receive(int id);

	Order suggest(int id);

	// 获取所有订单信息
	Map<Integer, Order> getOrders();

}
