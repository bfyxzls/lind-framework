package com.lind.common.collection;

import org.junit.jupiter.api.Test;
import org.testng.Assert;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author lind
 * @date 2023/2/7 15:45
 * @since 1.0.0
 */
public class SetTest {

	ConcurrentMap<String, Long> map = new ConcurrentHashMap<>();

	@Test
	public void isNotExist() {
		for (long i = 0; i < 10; i++) {
			map.putIfAbsent("zero", i);
		}
		Assert.assertTrue(map.get("zero").equals(0L));
	}

}
