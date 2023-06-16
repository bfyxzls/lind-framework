package com.lind.common;

import com.sun.media.jfxmedia.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

/**
 * @author lind
 * @date 2023/5/18 15:42
 * @since 1.0.0
 */
@Component
@Slf4j
public class RetryService {

	// multiplier表示下次重试时间是上次的2倍
	@Retryable(maxAttempts = 5, backoff = @Backoff(delay = 1000L, multiplier = 2))
	public void print() {
		log.info("print...");
		throw new IllegalArgumentException("hello world");
	}

}
