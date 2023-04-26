package com.lind.common.pattern.state.demo;

public interface State {

	void next(Context context);

	void prev(Context context);

	void printStatus();

}
