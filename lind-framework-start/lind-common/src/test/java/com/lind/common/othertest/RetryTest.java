package com.lind.common.othertest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author lind
 * @date 2023/5/18 15:34
 * @since 1.0.0
 */
@SpringBootTest()
@EnableRetry()
public class RetryTest {

	@Autowired
	RetryService retryService;

	@Test
	public void test() {
		retryService.print();
	}

}
