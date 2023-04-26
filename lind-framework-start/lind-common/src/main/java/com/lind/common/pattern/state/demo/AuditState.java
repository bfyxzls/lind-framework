package com.lind.common.pattern.state.demo;

public class AuditState implements State {

	@Override
	public void next(Context context) {
		context.setState(new StopState());
	}

	@Override
	public void prev(Context context) {
		context.setState(new StartState());
	}

	@Override
	public void printStatus() {
		System.out.println("审批");
	}

}
