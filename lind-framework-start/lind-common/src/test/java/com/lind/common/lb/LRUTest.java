package com.lind.common.lb;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author lind
 * @date 2024/1/26 16:58
 * @since 1.0.0
 */
@Slf4j
public class LRUTest {

	@Test
	public void lru() {
		LinkedHashMap lruItem = new LinkedHashMap<String, String>(16, 0.75f, true) {
			@Override
			protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
				if (super.size() > 5) {
					return true;
				}
				else {
					return false;
				}
			}
		};
		for (int i = 0; i < 10; i++) {
			lruItem.putIfAbsent("a:" + i, i);
			log.info("i={},set={}", i, lruItem);
		}
	}

}
