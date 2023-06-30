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

public class BigGiftHandler implements Handler {

	private static final Logger logger = LoggerFactory.getLogger(CouponHandler.class);

	private static final double DISCOUNT = .7;

	private Handler nextHandler;

	@Override
	public void handleRequest(Order order) {
		if (order.getTotalPrice() >= 1000) {
			// 应用折扣，折扣为标准的8折
			order.setTotalPrice(order.getTotalPrice() * DISCOUNT);
			logger.info("BigGift 0.7 applied to order,{}", order.getOrderId());
		}
		else if (nextHandler != null) {
			nextHandler.handleRequest(order);
		}

	}

	public void setNextHandler(Handler nextHandler) {
		this.nextHandler = nextHandler;
	}

}
