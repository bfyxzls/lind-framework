package com.lind.common.bean.postprocessor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

/**
 * @ContextConfiguration Spring整合JUnit4测试时，使用注解引入多个配置文件
 */
@ContextConfiguration(classes = Config.class)
public class PTest {

	@Autowired
	Context context;

	@Test
	public void dicList() {
		context.print();
	}

}
