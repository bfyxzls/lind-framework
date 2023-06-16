package com.lind.common.wheel;

import io.netty.util.HashedWheelTimer;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author lind
 * @date 2023/6/9 14:44
 * @since 1.0.0
 */
@Slf4j
public class OrderCloseTest {

	private final Map<String, Order> orders;

	private HashedWheelTimer timeWheel;

	private CountDownLatch countDownLatch = new CountDownLatch(2);

	public OrderCloseTest() {
		// 初始化时间轮，每秒一个槽，总共60个槽
		timeWheel = new HashedWheelTimer(1, TimeUnit.SECONDS, 16);
		this.orders = new HashMap<>();
	}

	@Test
	public void main() throws InterruptedException, IOException {
		log.info("程序启动");
		OrderCloseTest manager = new OrderCloseTest();
		manager.createOrder("order1", 5000); // 创建订单1，5秒后超时关闭
		manager.createOrder("order2", 8000); // 创建订单2，8秒后超时关闭
		countDownLatch.await();
		timeWheel.stop();
	}

	public void createOrder(String orderId, long timeoutMs) {
		Order order = new Order(orderId);
		orders.put(orderId, order);
		log.info("建立了订单:{}", orderId);

		// 将订单添加到时间轮，设定超时时间
		timeWheel.newTimeout((timeout) -> {
			closeOrder(orderId);
			countDownLatch.countDown();
		}, timeoutMs, TimeUnit.MILLISECONDS);
	}

	public void closeOrder(String orderId) {
		Order order = orders.remove(orderId);
		if (order != null) {
			log.info("Closing order: " + orderId);
			// 执行关闭订单的业务逻辑
		}
	}

	private static class Order {

		private final String orderId;

		public Order(String orderId) {
			this.orderId = orderId;
		}

	}

}
