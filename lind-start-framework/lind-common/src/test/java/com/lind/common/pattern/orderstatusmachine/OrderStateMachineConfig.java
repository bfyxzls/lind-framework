package com.lind.common.pattern.orderstatusmachine;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.support.DefaultStateMachineContext;

import java.util.EnumSet;

/**
 * @author lind
 * @date 2023/2/28 14:19
 * @since 1.0.0
 */
@Configuration
@EnableStateMachine(name = "orderStateMachine")
public class OrderStateMachineConfig extends StateMachineConfigurerAdapter<OrderStatus, OrderStatusChangeEvent> {

	/**
	 * 配置状态
	 * @param states
	 * @throws Exception
	 */
	public void configure(StateMachineStateConfigurer<OrderStatus, OrderStatusChangeEvent> states) throws Exception {
		states.withStates().initial(OrderStatus.WAIT_PAYMENT).states(EnumSet.allOf(OrderStatus.class));
	}

	/**
	 * 配置状态转换事件关系. 操作严格按着状态进行，没有到达某个状态时，操作是被禁止的.
	 * @param transitions
	 * @throws Exception
	 */
	public void configure(StateMachineTransitionConfigurer<OrderStatus, OrderStatusChangeEvent> transitions)
			throws Exception {
		transitions.withExternal().source(OrderStatus.WAIT_PAYMENT).target(OrderStatus.WAIT_DELIVER)
				.event(OrderStatusChangeEvent.PAYED);
		transitions.withExternal().source(OrderStatus.WAIT_DELIVER).target(OrderStatus.WAIT_RECEIVE)
				.event(OrderStatusChangeEvent.DELIVERY);
		transitions.withExternal().source(OrderStatus.WAIT_RECEIVE).target(OrderStatus.WAIT_SUGGEST)
				.event(OrderStatusChangeEvent.RECEIVED);
		transitions.withExternal().source(OrderStatus.WAIT_SUGGEST).target(OrderStatus.FINISH)
				.event(OrderStatusChangeEvent.SUGGESTED);
	}

	/**
	 * 持久化配置 实际使用中，可以配合redis等，进行持久化操作
	 * @return
	 */
	@Bean
	public DefaultStateMachinePersister persister() {
		return new DefaultStateMachinePersister<>(new StateMachinePersist<Object, Object, Order>() {
			@Override
			public void write(StateMachineContext<Object, Object> context, Order order) throws Exception {
				// 此处可以对订单状态进行持久化操作
				System.out.println("持久化-写入：" + order.getId());
			}

			@Override
			public StateMachineContext<Object, Object> read(Order order) throws Exception {
				// 此处直接获取order中的状态，其实并没有进行持久化读取操作
				System.out.println("持久化-读取：" + order.getId());

				return new DefaultStateMachineContext(order.getStatus(), null, null, null);
			}
		});
	}

}
