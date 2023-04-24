package com.lind.common.pattern.orderstatusmachine;

import org.springframework.messaging.Message;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;

/**
 * @author lind
 * @date 2023/2/28 14:19
 * @since 1.0.0
 */
@Component
@WithStateMachine(name = "orderStateMachine")
public class OrderStateListenerImpl {

	@OnTransition(source = "WAIT_PAYMENT", target = "WAIT_DELIVER")
	public boolean payTransition(Message<OrderStatusChangeEvent> message) {
		Order order = (Order) message.getHeaders().get("order");
		order.setStatus(OrderStatus.WAIT_DELIVER);
		System.out.println("支付，状态机反馈信息：" + message.getHeaders().toString());
		return true;
	}

	@OnTransition(source = "WAIT_DELIVER", target = "WAIT_RECEIVE")
	public boolean deliverTransition(Message<OrderStatusChangeEvent> message) {
		Order order = (Order) message.getHeaders().get("order");
		order.setStatus(OrderStatus.WAIT_RECEIVE);
		System.out.println("发货，状态机反馈信息：" + message.getHeaders().toString());
		return true;
	}

	@OnTransition(source = "WAIT_RECEIVE", target = "WAIT_SUGGEST")
	public boolean receiveTransition(Message<OrderStatusChangeEvent> message) {
		Order order = (Order) message.getHeaders().get("order");
		order.setStatus(OrderStatus.WAIT_SUGGEST);
		System.out.println("收货，状态机反馈信息：" + message.getHeaders().toString());
		return true;
	}

	@OnTransition(source = "WAIT_SUGGEST", target = "FINISH")
	public boolean suggestTransition(Message<OrderStatusChangeEvent> message) {
		Order order = (Order) message.getHeaders().get("order");
		order.setStatus(OrderStatus.FINISH);
		System.out.println("评价，状态机反馈信息：" + message.getHeaders().toString());
		return true;
	}

}
