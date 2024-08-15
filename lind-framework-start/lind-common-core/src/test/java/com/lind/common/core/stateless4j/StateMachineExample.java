package com.lind.common.core.stateless4j;

/**
 * @author lind
 * @date 2023/6/25 14:14
 * @since 1.0.0
 */

import lombok.var;
import org.junit.jupiter.api.Test;

public class StateMachineExample {

	private static void showSongsList() {
		System.out.println("Showing song list:\n\t1. La la la\n\t2. Guli guli guli\n\t3. A Ram Sam Sam");
	}

	private static void hideSongsList() {
		System.out.println("Hiding song list");
	}

	private static void consumeCoin() {
		System.out.println("Coin consumed");
	}

	private static void returnCoin() {
		System.out.println("Coin returned");
	}

	private static void showTimer() {
		System.out.println("Showing song timer");
	}

	private static void hideTimer() {
		System.out.println("Hiding song timer");
	}

	private static void playSong() {
		System.out.println("Playing selected song...");
	}

	private static void pauseSong() {
		System.out.println("Pausing...");
	}

	private static void startBlinking() {
		System.out.println("Start blinking: blink, blonk, blink, blonk...");
	}

	private static void stopBlinking() {
		System.out.println("Stop blinking");
	}

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

	@Test
	public void stateExample() {
		var config = new StateMachineConfig<State, Trigger>();

		config.configure(State.IDLE).onEntry(StateMachineExample::startBlinking)
				.onExit(StateMachineExample::stopBlinking).permit(Trigger.COIN_INSERTED, State.SELECTION);

		config.configure(State.SELECTION).onEntry(StateMachineExample::showSongsList)
				.onExit(StateMachineExample::hideSongsList)
				.permit(Trigger.START, State.PLAYING, StateMachineExample::consumeCoin)
				.permit(Trigger.CANCEL, State.IDLE, StateMachineExample::returnCoin);

		config.configure(State.RUNNING).onEntry(StateMachineExample::showTimer).onExit(StateMachineExample::hideTimer)
				.permit(Trigger.STOP, State.IDLE);

		config.configure(State.PLAYING).substateOf(State.RUNNING).onEntry(StateMachineExample::playSong)
				.permit(Trigger.PAUSE, State.PAUSED);

		config.configure(State.PAUSED).substateOf(State.RUNNING).onEntry(StateMachineExample::pauseSong)
				.permit(Trigger.RESUME, State.PLAYING);

		var fsm = new StateMachine<>(State.IDLE, config);
		fsm.fireInitialTransition();
		fsm.fire(Trigger.COIN_INSERTED);
		fsm.fire(Trigger.CANCEL);
		fsm.fire(Trigger.COIN_INSERTED);
		fsm.fire(Trigger.START);
		fsm.fire(Trigger.PAUSE);
		fsm.fire(Trigger.RESUME);
		fsm.fire(Trigger.STOP);
	}

	private enum State {

		IDLE, SELECTION, RUNNING, PLAYING, PAUSED

	}

	private enum Trigger {

		COIN_INSERTED, CANCEL, STOP, START, PAUSE, RESUME

	}

}
