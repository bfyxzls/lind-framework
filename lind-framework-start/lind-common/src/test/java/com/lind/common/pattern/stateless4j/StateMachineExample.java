package com.lind.common.pattern.stateless4j;

/**
 * @author lind
 * @date 2023/6/25 14:14
 * @since 1.0.0
 */

import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.StateMachineConfig;

public class StateMachineExample {

	public static void main(String[] args) {
		// 创建状态机配置
		StateMachineConfig<State, Trigger> config = new StateMachineConfig<>();

		// 挂机->【拨号】->响铃
		config.configure(State.OFF_HOOK).permit(Trigger.CALL_DIALED, State.RINGING);

		// 响铃->【挂了】->挂机；响铃->【接通】->连接
		config.configure(State.RINGING).permit(Trigger.HUNG_UP, State.OFF_HOOK).permit(Trigger.CALL_CONNECTED,
				State.CONNECTED);
		// 连接->【留言】->挂机；连接->【通话】->待机
		config.configure(State.CONNECTED).permit(Trigger.LEFT_MESSAGE, State.OFF_HOOK)
				.permit(Trigger.HUNG_UP, State.OFF_HOOK).permit(Trigger.PLACED_ON_HOLD, State.ON_HOLD);

		// 待机->【恢复通话】->连接；待机->【挂了】->空闲
		config.configure(State.ON_HOLD).permit(Trigger.TAKEN_OFF_HOLD, State.CONNECTED).permit(Trigger.HUNG_UP,
				State.OFF_HOOK);

		// 创建状态机实例
		StateMachine<State, Trigger> stateMachine = new StateMachine<>(State.OFF_HOOK, config);

		// 执行状态转换,目前当前状态是OFF_HOOK，只有trigger是CALL_DIALED才是有效的
		stateMachine.fire(Trigger.CALL_DIALED);
		// stateMachine.fire(Trigger.CALL_CONNECTED);
		// stateMachine.fire(Trigger.PLACED_ON_HOLD);
		// stateMachine.fire(Trigger.TAKEN_OFF_HOLD);
		// stateMachine.fire(Trigger.HUNG_UP);

		// 获取当前状态
		State currentState = stateMachine.getState();
		// 执行数据库操作，将最新的状态 newState 存储到数据库中
		System.out.println("需要把状态更新到数据库——State: " + currentState);
	}

	enum State {

		/**
		 * 表示电话机已经从底座拿起，但用户还没有拨打或者接听电话，此时电话处于空闲状态
		 */
		OFF_HOOK,
		/**
		 * 响铃
		 */
		RINGING,
		/**
		 * 连接
		 */
		CONNECTED,
		/**
		 * 表示通话的一方需要暂时挂起通话
		 */
		ON_HOLD

	}

	enum Trigger {

		/**
		 * 用户拨打电话并等待接听
		 */
		CALL_DIALED,
		/**
		 * 表示通话结束后挂断电话
		 */
		HUNG_UP,
		/**
		 * 表示接听方接通电话，此时双方正常通话。
		 */
		CALL_CONNECTED,
		/**
		 * 表示通话的一方需要暂时挂起通话
		 */
		PLACED_ON_HOLD,
		/**
		 * 表示恢复保持状态执行通话【与PLACED_ON_HOLD对应】
		 */
		TAKEN_OFF_HOLD,
		/**
		 * 留言
		 */
		LEFT_MESSAGE,
		/**
		 * 表示通话的一方结束通话并停止使用电话
		 */
		STOP_USING_PHONE

	}

}
