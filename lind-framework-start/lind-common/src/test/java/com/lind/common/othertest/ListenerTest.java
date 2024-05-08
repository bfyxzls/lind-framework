package com.lind.common.othertest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest()
public class ListenerTest {

	@Autowired
	ApplicationEventPublisher applicationEventPublisher;

}
