package com.lind.common.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class A1EventListener {

	@EventListener(A1Event.class)
	public void saveFlagInfo(A1Event flagSaveEvent) {
		System.out.println("A1:" + flagSaveEvent.getSource());
	}

}
