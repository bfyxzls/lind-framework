package com.lind.common.collection;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lind
 * @date 2022/8/17 16:05
 * @since 1.0.0
 */
@Slf4j
public class MapAndFlatMapTest {

	@Test
	public void map() {
		List<List<Integer>> lists = new ArrayList<>();
		List<Integer> list = new ArrayList<>();
		list.add(4444);
		list.add(33333);
		list.add(444444);
		lists.add(list);
		lists.stream().map(Collection::stream).forEach(System.out::println);
	}

	/**
	 * List<List<String>>这种嵌套的会用它
	 */
	@Test
	public void flatMap() {
		List<List<Integer>> lists = new ArrayList<>();
		List<Integer> list = new ArrayList<>();
		list.add(4444);
		list.add(33333);
		list.add(444444);
		lists.add(list);
		lists.stream().flatMap(Collection::stream).forEach(System.out::println);
	}

	// computeIfPresent对hashMap中指定 key 的值进行重新计算，前提是该 key 存在于 hashMap 中。
	@Test
	public void compute() {
		Map<String, Integer> timeoutMap = new ConcurrentHashMap<>();
		timeoutMap.put("lr", 1);
		timeoutMap.put("zzl", 10);
		timeoutMap.computeIfPresent("zzl", (key1, curTimeout) -> {
			log.info("key={}", key1);
			curTimeout += 1;
			return curTimeout;
		});
		log.info("{}", timeoutMap);
	}

}
