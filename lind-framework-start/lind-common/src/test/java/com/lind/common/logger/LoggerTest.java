package com.lind.common.logger;

import com.lind.common.othertest.AbstractTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class LoggerTest extends AbstractTest {

	@Test
	public void filter() {
		log.debug("hello world!");
		log.info("hello world!");
		log.warn("warning end.");
		log.error("error end.");

	}

}
