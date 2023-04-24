package com.lind.logback;

import com.lind.logback.appender.MapHolder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.stream.IntStream;

/**
 * @author lind
 * @date 2023/1/29 17:40
 * @since 1.0.0
 */
@Slf4j
public class TestAppend {

	@Test
	public void mainss() {
		IntStream.rangeClosed(1, 10).forEach(counter -> {
			log.info("Counter:" + counter);
		});
		MapHolder.create().getEventMap().values().forEach((value) -> {
			log.info("value:" + value);
		});
	}

}
