package com.lind.common.event;

import com.lind.common.core.util.SpringContextUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest()
public class ApplicationEventTest {

	@Test
	public void applicationEvent() {
		SpringContextUtils.publishEvent(new A1Event("lind"));
	}

}
