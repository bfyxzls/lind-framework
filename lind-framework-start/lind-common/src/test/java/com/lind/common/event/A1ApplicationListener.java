package com.lind.common.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author lind
 * @date 2023/5/4 13:14
 * @since 1.0.0
 */
@Component
@Slf4j
public class A1ApplicationListener implements ApplicationListener<A1Event> {

	@Override
	public void onApplicationEvent(A1Event event) {
		log.info("A1ApplicationListener:{}", event);
	}

}
