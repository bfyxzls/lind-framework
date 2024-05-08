package com.lind.common.bean.conditional.onbean;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author lind
 * @date 2023/9/20 21:46
 * @since 1.0.0
 */
@SpringBootTest()
public class RegisterBeanConditionalTest {

	@Autowired
	Cat cat;

	@Test
	public void catTest() {
		cat.run();
	}

}
