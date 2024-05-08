package com.lind.common.ratelimit;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author lind
 * @date 2023/12/19 15:14
 * @since 1.0.0
 */
@Slf4j
public class WindowLimitUtilTest {

	@Test
	public void run10Qps() {
		for (int i = 0; i < 11; i++) {
			boolean result = new WindowLimitUtil().fixedWindowsTryAcquire();
			log.info("{} {}", i, result);
		}
	}

}
