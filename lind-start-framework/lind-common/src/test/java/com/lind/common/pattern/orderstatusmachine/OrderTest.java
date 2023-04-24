package com.lind.common.pattern.orderstatusmachine;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author lind
 * @date 2023/2/28 14:30
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderTest {

	@Autowired
	IOrderService orderService;

	@Test
	public void flow() {
		orderService.create();
		orderService.pay(1);
		orderService.deliver(1);
		orderService.receive(1);
		orderService.suggest(1);

	}

}
