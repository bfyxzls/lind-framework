package com.lind.common.pattern.springmachine;

import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;

import java.util.EnumSet;

public class StateMachineExample {

	enum State {

		OFF_HOOK, RINGING, CONNECTED, ON_HOLD

	}

	enum Trigger {

		CALL_DIALED, HUNG_UP, CALL_CONNECTED, PLACED_ON_HOLD, TAKEN_OFF_HOLD, LEFT_MESSAGE, STOP_USING_PHONE

	}

	public static void main(String[] args) throws Exception {
		// 创建状态机
		StateMachine<State, Trigger> stateMachine = buildStateMachine();

		// 执行状态转换
		stateMachine.sendEvent(Trigger.CALL_DIALED);
		stateMachine.sendEvent(Trigger.CALL_CONNECTED);
		stateMachine.sendEvent(Trigger.PLACED_ON_HOLD);
		stateMachine.sendEvent(Trigger.TAKEN_OFF_HOLD);
		stateMachine.sendEvent(Trigger.HUNG_UP);

		// 获取当前状态
		State currentState = stateMachine.getState().getId();
		System.out.println("Current State: " + currentState);
	}

	private static StateMachine<State, Trigger> buildStateMachine() throws Exception {
		// 创建状态机构建器
		StateMachineBuilder.Builder<State, Trigger> builder = StateMachineBuilder.builder();

		// 配置状态和触发器
		builder.configureStates().withStates().initial(State.OFF_HOOK).states(EnumSet.allOf(State.class));
		builder.configureTransitions().withExternal().source(State.OFF_HOOK).target(State.RINGING)
				.event(Trigger.CALL_DIALED).and().withExternal().source(State.RINGING).target(State.OFF_HOOK)
				.event(Trigger.HUNG_UP).and().withExternal().source(State.RINGING).target(State.CONNECTED)
				.event(Trigger.CALL_CONNECTED).and().withExternal().source(State.CONNECTED).target(State.OFF_HOOK)
				.event(Trigger.LEFT_MESSAGE).and().withExternal().source(State.CONNECTED).target(State.OFF_HOOK)
				.event(Trigger.HUNG_UP).and().withExternal().source(State.CONNECTED).target(State.ON_HOLD)
				.event(Trigger.PLACED_ON_HOLD).and().withExternal().source(State.ON_HOLD).target(State.CONNECTED)
				.event(Trigger.TAKEN_OFF_HOLD).and().withExternal().source(State.ON_HOLD).target(State.OFF_HOOK)
				.event(Trigger.HUNG_UP);

		// 构建状态机
		return builder.build();
	}

}
