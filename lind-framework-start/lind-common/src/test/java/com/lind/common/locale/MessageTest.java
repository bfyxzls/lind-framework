package com.lind.common.locale;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author lind
 * @date 2023/10/8 17:22
 * @since 1.0.0
 */
@SpringBootTest
public class MessageTest {

	static final Logger logger = LoggerFactory.getLogger(MessageTest.class);

	@Test
	public void readConfigMsg() {
		logger.info(LocaleMessageUtils.getMessage("sys.user.update.passwordError"));
	}

}
