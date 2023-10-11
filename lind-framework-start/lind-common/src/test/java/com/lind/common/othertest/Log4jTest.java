package com.lind.common.othertest;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class Log4jTest {

	@Test
	public void maxSize() {
		log.error("严重警告");
		log.warn("警告");
		log.info("普通信息");
		log.debug("调试信息");
	}

}
