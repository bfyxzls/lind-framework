package com.lind.common.othertest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class ModifyConfigProperty {

	@Test
	public void read() {
		System.out.println(System.getProperty("logging.file.name"));
	}

}
