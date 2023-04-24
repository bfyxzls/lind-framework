package com.lind.common.bean.postprocessor;

import org.springframework.stereotype.Component;

@Component
public class EmailSend extends AbstractSend {

	@Override
	void send(String message) {
		System.out.println("email send message");
	}

}
