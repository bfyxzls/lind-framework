package com.lind.common.pattern.chinaorder;

import com.lind.common.pattern.chinaorder.handler.CouponHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lind
 * @date 2023/6/27 16:15
 * @since 1.0.0
 */
public class Test {

	private static final Logger logger = LoggerFactory.getLogger(CouponHandler.class);

	public static void main(String[] args) {
		HandlerModel handlerModel = new HandlerModel();
		handlerModel.setSort(1).setClassPath("");// @Accessors(chain = true) 链式变成风格
		Handler couponHandler = HandlerFactory.handlerFactory();

		Order order1 = new Order("OR01", 150, true);
		couponHandler.handleRequest(order1);
		logger.info("order1:{}\n", order1.getTotalPrice());

		Order order2 = new Order("OR02", 250, true);
		couponHandler.handleRequest(order2);
		logger.info("order2:{}\n", order2.getTotalPrice());

		Order order3 = new Order("OR03", 50, true);
		couponHandler.handleRequest(order3);
		logger.info("order3:{}\n", order3.getTotalPrice());

		Order order4 = new Order("OR04", 5001, true);
		couponHandler.handleRequest(order4);
		logger.info("order4:{}\n", order4.getTotalPrice());

		Order order5 = new Order("OR05", 10001, true);
		couponHandler.handleRequest(order5);
		logger.info("order5:{}\n", order5.getTotalPrice());
		/*
		 * 在上述示例中，我们使用责任链模式来处理订单的优惠活动。CouponHandler和DiscountHandler分别代表应用优惠券和折扣的处理者。
		 * 在Main类中，我们创建了这两个具体的处理者对象，并设置了它们之间的责任链关系。然后，我们创建了两个订单对象，分别表示总价为150和250。
		 * 通过调用couponHandler.handleRequest()方法，订单会依次经过责任链中的处理者进行处理。如果订单满足某个处理者的条件，该处理者会应用\
		 * 相应的优惠活动。
		 *
		 * 当运行示例代码时，根据订单的总价，责任链中的处理者会应用相应的优惠活动。对于订单1（总价为150），CouponHandler会应用优惠券，
		 * 而对于订单2（总价为250），DiscountHandler会应用折扣。如果订单不满足任何处理
		 */

	}

}
