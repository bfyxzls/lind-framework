package com.lind.common.core.stateless4j;

public class StateReference<S, T> {

	private S state;

	public S getState() {
		return state;
	}

	public void setState(S value) {
		state = value;
	}

}
