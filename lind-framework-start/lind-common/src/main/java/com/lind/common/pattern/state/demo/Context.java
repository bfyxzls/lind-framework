package com.lind.common.pattern.state.demo;

public class Context {

	private State state;

	public Context(State state) {
		this.state = state;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public void prev() {
		state.prev(this);
	}

	public void next() {
		state.next(this);
	}

	public void printStatus() {
		state.printStatus();
	}

}
