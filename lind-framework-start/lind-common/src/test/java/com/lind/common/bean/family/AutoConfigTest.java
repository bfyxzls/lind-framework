package com.lind.common.bean.family;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author lind
 * @date 2023/9/25 10:17
 * @since 1.0.0
 */
@SpringBootTest
public class AutoConfigTest {

	@Autowired
	Son son;

	@Test
	public void test() {
		System.out.println("测试");
	}

}
