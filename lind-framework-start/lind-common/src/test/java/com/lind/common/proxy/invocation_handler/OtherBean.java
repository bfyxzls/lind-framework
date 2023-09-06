package com.lind.common.proxy.invocation_handler;

import org.springframework.stereotype.Component;

/**
 * @author lind
 * @date 2023/9/5 15:21
 * @since 1.0.0
 */
@Component
public class OtherBean {

	public void print() {
		System.out.println("OtherBean.print...");
	}

}
