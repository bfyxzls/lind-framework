package com.lind.common.proxy;

import com.lind.common.proxy.anno.EnableMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
