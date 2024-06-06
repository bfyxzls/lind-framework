package com.lind.common.core.stateless4j;

/**
 * @author lind
 * @date 2023/6/25 14:14
 * @since 1.0.0
 */

import org.junit.jupiter.api.Test;

public class StateMachineExample {

	@Test
	public void testStateless4j() {
		StateMachineConfig<String, String> stateMachineConfig = new StateMachineConfig<>();
		stateMachineConfig.configure("待付款").permit("用户付款", "待发货");
		stateMachineConfig.configure("待发货").permit("卖家发货", "待收货");
		stateMachineConfig.configure("待收货").permit("买家签收", "待评价");
		stateMachineConfig.configure("待评价").permit("买家点评", "订单关闭");

		// 创建状态机实例
		StateMachine<String, String> stateMachine = new StateMachine<>("待发货", stateMachineConfig);
		// stateMachine.fire("买家签收");//java.lang.IllegalStateException: No valid leaving
		// transitions are permitted from state
		stateMachine.fire("卖家发货");
		System.out.println("当前状态：" + stateMachine.getState());
	}

}
