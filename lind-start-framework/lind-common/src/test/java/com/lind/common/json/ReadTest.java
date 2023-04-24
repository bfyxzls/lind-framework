package com.lind.common.json;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Map;

/**
 * @author lind
 * @date 2023/2/8 10:18
 * @since 1.0.0
 */
public class ReadTest {

	static final Logger logger = LoggerFactory.getLogger(ReadTest.class);

	private static String trimTrailingCharacter(String string, char c) {
		if (string.length() > 0 && string.charAt(string.length() - 1) == c) {
			return string.substring(0, string.length() - 1);
		}
		return string;
	}

	private static String trimLeadingCharacter(String string, char c) {
		if (string.length() > 0 && string.charAt(0) == c) {
			return string.substring(1);
		}
		return string;
	}

	@Test
	public void readJsonString() {
		DemoEntity entity = DemoEntity.builder().title("test-1").count(10)
				.sons(Arrays.asList(DemoEntity.builder().title("test-1-1").count(1).build())).build();
		String result = BasicJson.toJson(entity);
		logger.debug("json={}", result);
		Map map = BasicJson.parseMap(result);
		logger.debug("map={}", map);
	}

	@Test
	public void leadingAndTrialing() {
		String msg = "(a=123)";
		String result = trimLeadingCharacter(trimTrailingCharacter(msg, ')'), '(');
		logger.debug("result:{}", result);
		trimTrailingCharacter(null, ')');
	}

}
