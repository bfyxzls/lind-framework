package com.lind.common.othertest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest()
public class ModifyConfigProperty {

	@Test
	public void read() {
		System.out.println(System.getProperty("logging.file.name"));
	}

}
