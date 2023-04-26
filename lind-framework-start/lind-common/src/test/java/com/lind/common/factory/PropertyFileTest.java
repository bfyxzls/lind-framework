package com.lind.common.factory;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author lind
 * @date 2022/7/1 17:21
 * @description
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class PropertyFileTest {

	@Value("${author}")
	String author;

	@Test
	public void read() {
		log.info("author={}", author);
	}

}
