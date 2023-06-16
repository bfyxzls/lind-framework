package com.lind.common.thread;

import com.lind.common.config.TaskExecutorConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.Executor;

/**
 * @author lind
 * @date 2023/5/25 14:58
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TaskExecutorConfiguration.class })
@Slf4j
public class ExecutorTest {

	@Autowired
	Executor executor;

	@Test
	public void hello() {
		executor.execute(() -> {
			log.info("hello world");
		});
	}

}
