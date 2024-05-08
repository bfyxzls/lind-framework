package com.lind.common.pattern.lockstatusmachine;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author lind
 * @date 2023/2/28 17:48
 * @since 1.0.0
 */
@SpringBootTest
public class StatemachineTest {

	@Autowired
	private StateMachine<TurnstileStates, TurnstileEvents> stateMachine;

	@Test
	public void test() {
		stateMachine.start();
		stateMachine.sendEvent(TurnstileEvents.LEADER_PASS);
		stateMachine.sendEvent(TurnstileEvents.MGR_PASS);
		stateMachine.sendEvent(TurnstileEvents.HR_PASS);
		stateMachine.stop();
	}

	@Test
	public void fail() {
		stateMachine.start();
		stateMachine.sendEvent(TurnstileEvents.LEADER_PASS);
		stateMachine.sendEvent(TurnstileEvents.HR_PASS);
		stateMachine.stop();
	}

}
