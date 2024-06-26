package com.lind.common.bean.application_context_aware;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author lind
 * @date 2022/9/9 9:44
 * @since 1.0.0
 */
@SpringBootTest()
public class TestAware {

	@Autowired
	LindAware lindAware;

	@Test
	public void print() {
		lindAware.contextPrint();
	}

}
