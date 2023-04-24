package com.lind.common.pattern.lockstatusmachine;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

/**
 * @author lind
 * @date 2023/2/28 17:46
 * @since 1.0.0
 */
@Configuration
@EnableStateMachine(name = "turnstileStateMachine1")
public class StatemachineConfigurer extends EnumStateMachineConfigurerAdapter<TurnstileStates, TurnstileEvents> {

	@Override
	public void configure(StateMachineStateConfigurer<TurnstileStates, TurnstileEvents> states) throws Exception {
		states.withStates()
				// 初识状态：Locked
				.initial(TurnstileStates.Wait_Leader).states(EnumSet.allOf(TurnstileStates.class));
	}

	@Override
	public void configure(StateMachineTransitionConfigurer<TurnstileStates, TurnstileEvents> transitions)
			throws Exception {
		transitions.withExternal().source(TurnstileStates.Wait_Leader).target(TurnstileStates.Wait_Manager)
				.event(TurnstileEvents.LEADER_PASS).action(leader()).and().withExternal()
				.source(TurnstileStates.Wait_Manager).target(TurnstileStates.Wait_HR).event(TurnstileEvents.MGR_PASS)
				.action(manager()).and().withExternal().source(TurnstileStates.Wait_HR).target(TurnstileStates.Finish)
				.event(TurnstileEvents.HR_PASS).action(hr());
	}

	public Action<TurnstileStates, TurnstileEvents> leader() {
		return context -> System.out.println("主管审批");
	}

	public Action<TurnstileStates, TurnstileEvents> manager() {
		return context -> System.out.println("经理审核");
	}

	public Action<TurnstileStates, TurnstileEvents> hr() {
		return context -> System.out.println("hr最后审核");
	}

}
