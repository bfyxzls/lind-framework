package com.lind.common.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author lind
 * @date 2022/7/4 11:42
 * @description
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest()
public class LocaleMessageUtilsTest {

	@Test
	public void messageSource() {
		// log.info(LocaleMessageUtils.getMessage("sys.user.update.passwordError"));
	}

}
