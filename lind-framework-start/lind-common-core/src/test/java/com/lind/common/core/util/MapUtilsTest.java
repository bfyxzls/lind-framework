package com.lind.common.core.util;

import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * @author lind
 * @date 2023/7/13 14:59
 * @since 1.0.0
 */
public class MapUtilsTest {

	@Test
	public void test() {
		Map<String, String> map = MapUtils.hashMapBuilder("a", "1").put("b", "2").put("c", "3").build();
		System.out.println(map);
	}

}
