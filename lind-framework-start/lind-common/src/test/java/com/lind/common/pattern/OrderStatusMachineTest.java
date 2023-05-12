package com.lind.common.pattern;

import org.junit.Test;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lind
 * @date 2023/5/12 15:40
 * @since 1.0.0
 */
public class OrderStatusMachineTest {

	private static final Map<OrderStatus, Map<OrderEvent, OrderStatus>> statusMap = new ConcurrentHashMap<>();

	static {
		Map<OrderEvent, OrderStatus> createdMap = new EnumMap<>(OrderEvent.class);
		createdMap.put(OrderEvent.PAYMENT_PENDING, OrderStatus.PAYMENT_PENDING);
		createdMap.put(OrderEvent.CANCEL, OrderStatus.CANCELLED);
		statusMap.put(OrderStatus.CREATED, createdMap);

		Map<OrderEvent, OrderStatus> paymentPendingMap = new EnumMap<>(OrderEvent.class);
		paymentPendingMap.put(OrderEvent.PAYMENT_PROCESSING, OrderStatus.PAYMENT_PROCESSING);
		paymentPendingMap.put(OrderEvent.CANCEL, OrderStatus.CANCELLED);
		statusMap.put(OrderStatus.PAYMENT_PENDING, paymentPendingMap);

		Map<OrderEvent, OrderStatus> paymentProcessingMap = new EnumMap<>(OrderEvent.class);
		paymentProcessingMap.put(OrderEvent.PAYMENT_FAILED, OrderStatus.PAYMENT_FAILED);
		paymentProcessingMap.put(OrderEvent.PAYMENT_SUCCESSFUL, OrderStatus.PAYMENT_SUCCESSFUL);
		paymentProcessingMap.put(OrderEvent.CANCEL, OrderStatus.CANCELLED);
		statusMap.put(OrderStatus.PAYMENT_PROCESSING, paymentProcessingMap);

		Map<OrderEvent, OrderStatus> paymentFailedMap = new EnumMap<>(OrderEvent.class);
		paymentFailedMap.put(OrderEvent.PAYMENT_PENDING, OrderStatus.PAYMENT_PENDING);
		paymentFailedMap.put(OrderEvent.CANCEL, OrderStatus.CANCELLED);
		statusMap.put(OrderStatus.PAYMENT_FAILED, paymentFailedMap);

		Map<OrderEvent, OrderStatus> paymentSuccessfulMap = new EnumMap<>(OrderEvent.class);
		paymentSuccessfulMap.put(OrderEvent.COMPLETE, OrderStatus.COMPLETED);
		paymentSuccessfulMap.put(OrderEvent.CANCEL, OrderStatus.CANCELLED);
		statusMap.put(OrderStatus.PAYMENT_SUCCESSFUL, paymentSuccessfulMap);
	}

	@Test
	public void handleOrder() {
		Order order = new Order("1");
		order.handle(OrderEvent.PAYMENT_PENDING);
        order.handle(OrderEvent.PAYMENT_PROCESSING);

        System.out.println(order.getStatus());
	}

	public enum OrderStatus {

		CREATED, // 订单已创建
		PAYMENT_PENDING, // 支付待处理
		PAYMENT_PROCESSING, // 正在支付
		PAYMENT_FAILED, // 支付失败
		PAYMENT_SUCCESSFUL, // 支付成功
		CANCELLED, // 订单已取消
		COMPLETED // 订单已完成

	}

	public enum OrderEvent {

		CREATE, // 创建订单事件
		PAYMENT_PENDING, // 支付待处理事件
		PAYMENT_PROCESSING, // 正在支付事件
		PAYMENT_FAILED, // 支付失败事件
		PAYMENT_SUCCESSFUL, // 支付成功事件
		CANCEL, // 取消订单事件
		COMPLETE // 完成订单事件

	}

	public class Order {

		private final String orderId;
		private OrderStatus status;

		public Order(String orderId) {
			this.orderId = orderId;
			this.status = OrderStatus.CREATED;
		}

		public void handle(OrderEvent event) {
			Map<OrderEvent, OrderStatus> eventMap = OrderStatusMachineTest.statusMap.get(status);
			OrderStatus nextStatus = eventMap.get(event);
			if (nextStatus != null) {
				status = nextStatus;
			}
			else {
				throw new RuntimeException("Invalid event " + event + " for current status " + status);
			}
		}

		public OrderStatus getStatus() {
			return status;
		}

		public String getOrderId() {
			return orderId;
		}

	}

}
