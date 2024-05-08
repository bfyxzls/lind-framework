package com.lind.common.othertest;

import com.lind.common.aspect.repeat.EnableTryDo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest()
@EnableTryDo
public class AspectTest {

	@Autowired
	TestDemo aspectTest;

	@Test
	public void tryDo() throws InterruptedException {
		aspectTest.print();
		Thread.sleep(20 * 1000);
	}

}
