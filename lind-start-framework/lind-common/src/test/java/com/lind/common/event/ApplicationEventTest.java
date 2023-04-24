package com.lind.common.event;

import com.lind.common.util.SpringContextUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class ApplicationEventTest {

	@Test
	public void applicationEvent() {
		SpringContextUtils.publishEvent(new A1Event("lind"));
	}

}
