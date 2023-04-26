package com.lind.common.bean.factory;

import com.lind.common.bean.factory.core.SendProxyBeanDefinitionRegistry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest()
@Import(value = { SendProxyBeanDefinitionRegistry.class })
public class TestBean {

	@Autowired
	SendService sendService;

	@Test
	public void proxyTest() {
		sendService.insert(new User());
	}

}
