package com.lind.common.proxy;

import com.lind.common.proxy.anno.EnableMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableMessage(basePackages = { "com.lind.common.proxy" })
public class ProxyTest {

	@Autowired
	PeopleMessageProvider peopleMessageProvider;

	@Autowired
	OrderMessageProvider orderMessageProvider;

	@Test
	public void test() {
		peopleMessageProvider.send("lind");
		orderMessageProvider.send("lind");
	}

}
