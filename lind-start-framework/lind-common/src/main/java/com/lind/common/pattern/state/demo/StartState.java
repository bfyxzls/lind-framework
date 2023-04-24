package com.lind.common.pattern.state.demo;

public class StartState implements State {

	@Override
	public void next(Context context) {
		context.setState(new AuditState());

	}

	@Override
	public void prev(Context context) {
		System.out.println("这是初始状态");
	}

	@Override
	public void printStatus() {
		System.out.println("开始");
	}

}
