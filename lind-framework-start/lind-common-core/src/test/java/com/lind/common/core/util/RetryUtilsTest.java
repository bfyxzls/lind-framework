package com.lind.common.core.util;

import org.junit.jupiter.api.Test;

import java.util.Date;

public class RetryUtilsTest {

	@Test
	public void test5() {
		RetryUtils.run(() -> {
			System.out.println("test5 retry 5." + new Date());
			int a = 0;
			int b = 1 / a;

		});
	}

}
