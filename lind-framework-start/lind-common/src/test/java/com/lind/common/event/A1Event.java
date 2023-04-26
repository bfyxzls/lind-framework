package com.lind.common.event;

import org.springframework.context.ApplicationEvent;

public class A1Event extends ApplicationEvent {

	/**
	 * Create a new {@code ApplicationEvent}.
	 * @param source the object on which the com.lind.common.event initially occurred or
	 * with which the com.lind.common.event is associated (never {@code null})
	 */
	public A1Event(Object source) {
		super(source);
	}

}
