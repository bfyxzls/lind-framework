package com.lind.common.core.util;

import org.junit.jupiter.api.Test;

/**
 * @author lind
 * @date 2023/7/13 14:00
 * @since 1.0.0
 */
public class ConfigFileUtilsTest {

	@Test
	public void loadProperties() {
		System.out.println(ConfigFileUtils.getInstance().getStrPropertyValue("auth"));
		System.out.println(ConfigFileUtils.getInstance().getStrPropertyValue("title"));

	}

}
