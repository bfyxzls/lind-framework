package com.lind.common.bean.applicationcontextaware_init_disposable;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ContextConfiguration(classes = LindConfig.class)
public class Demo {

	@Autowired
	LindFactory lindFactory;

	@Test
	public void start() {
		lindFactory.start();
	}

}
