package com.lind.common.bean.postprocessor;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @ContextConfiguration Spring整合JUnit4测试时，使用注解引入多个配置文件
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
public class Test {

	@Autowired
	Context context;

	@org.junit.Test
	public void dicList() {
		context.print();
	}

}
