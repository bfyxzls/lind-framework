package com.lind.common.pattern.chinaorder.handler;

import com.lind.common.pattern.chinaorder.Handler;
import com.lind.common.pattern.chinaorder.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lind
 * @date 2023/6/27 16:14
 * @since 1.0.0
 */
public class CouponHandler implements Handler {

	private static final Logger logger = LoggerFactory.getLogger(CouponHandler.class);

	private Handler nextHandler;

	@Override
	public void handleRequest(Order order) {
		// 这是可以想用多个责任链的实现，如果是只使用1个，需要加上200的限制，就是每个订单只能现用1种优惠
		if (order.getTotalPrice() >= 100) {
			// 应用优惠券打9折
			order.setTotalPrice(order.getTotalPrice() * .9);
			logger.info("Coupon 0.9 applied to order,{}", order.getOrderId());
		}
		else if (nextHandler != null) {
			nextHandler.handleRequest(order);
		}
	}

	public void setNextHandler(Handler nextHandler) {
		this.nextHandler = nextHandler;
	}

}
