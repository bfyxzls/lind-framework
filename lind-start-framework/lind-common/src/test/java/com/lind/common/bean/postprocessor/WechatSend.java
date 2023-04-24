package com.lind.common.bean.postprocessor;

import org.springframework.stereotype.Component;

@Component
public class WechatSend extends AbstractSend {

	@Override
	void send(String message) {
		System.out.println("weixin send message");
	}

}
