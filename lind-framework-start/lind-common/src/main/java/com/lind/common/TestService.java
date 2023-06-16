package com.lind.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author lind
 * @date 2023/5/25 15:04
 * @since 1.0.0
 */
@Service
@Slf4j
public class TestService {

	@Async
	public void hello() throws InterruptedException {
		Thread.sleep(5000);
		log.info("hello world");
	}

}
