package com.lind.common.bean.application_context_aware;

import org.springframework.stereotype.Component;

/**
 * @author lind
 * @date 2022/9/9 9:41
 * @since 1.0.0
 */
@Component
public class LindContext {

	public void print() {
		System.err.println("lind-context print.");
	}

}
