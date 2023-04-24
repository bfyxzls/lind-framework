package com.lind.common.pattern.state.demo;

public class StopState implements State {

	@Override
	public void next(Context context) {
		System.out.println("这已经是结束了");

	}

	@Override
	public void prev(Context context) {
		context.setState(new AuditState());
	}

	@Override
	public void printStatus() {
		System.out.println("结束");

	}

}
