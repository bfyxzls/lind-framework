package com.lind.common.pattern.orderstatusmachine;

/**
 * @author lind
 * @date 2023/2/28 14:18
 * @since 1.0.0
 */
public enum OrderStatus {

	// 待支付 > 待发货 > 待收货 > 待评价 > 订单结束
	WAIT_PAYMENT, WAIT_DELIVER, WAIT_RECEIVE, WAIT_SUGGEST, FINISH;

}
