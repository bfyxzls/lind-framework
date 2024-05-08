package com.lind.common.factory;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author lind
 * @date 2022/7/1 17:21
 * @description
 */
@Slf4j
@SpringBootTest
public class PropertyFileTest {

	@Value("${author}")
	String author;

	@Test
	public void read() {
		log.info("author={}", author);
	}

}
