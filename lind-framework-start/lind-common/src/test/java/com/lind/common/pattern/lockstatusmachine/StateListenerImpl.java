package com.lind.common.pattern.lockstatusmachine;

import com.lind.common.pattern.orderstatusmachine.OrderStatusChangeEvent;
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
@WithStateMachine(name = "turnstileStateMachine1")
public class StateListenerImpl {

	@OnTransition(source = "Wait_Leader", target = "Wait_Manager")
	public boolean payTransition(Message<OrderStatusChangeEvent> message) {
		System.out.println("等待主管审评");
		return true;
	}

	@OnTransition(source = "Wait_Manager", target = "Wait_Hr")
	public boolean deliverTransition(Message<OrderStatusChangeEvent> message) {
		System.out.println("等待经理审评");
		return true;
	}

}
