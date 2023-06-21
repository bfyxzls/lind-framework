package com.lind.common.collection;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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

	@Test
	public void putIfAbsent() {
		Map<Integer, String> map = new HashMap<Integer, String>() {
			{
				put(3, "three");
			}
		};

		for (int i = 0; i < 10; i++) {
			// 与老版不同的是，putIfAbent() 方法在 put 之前， 不用在写if null continue了
			// 会判断 key 是否已经存在，存在则直接返回 value,不再会put了, 否则如果不存在，就put, 再返回 value
			map.putIfAbsent(i, "val" + i);
		}
		// forEach 可以很方便地对 map 进行遍历操作
		map.forEach((key, value) -> System.out.println(value));
	}

	@Test
	public void test27() {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>() {
			{
				put(1, 10);
				put(3, 30);
				put(9, 90);
			}
		};
		// 如下：对 key 为 3 的值，内部会先判断值是否存在，存在，则做 value + key 的拼接操作
		map.computeIfPresent(3, (num, val) -> val + num);
		log.info("3={}", map.get(3)); // val33

		// 先判断 key 为 9 的元素是否存在，存在，则做删除操作
		map.computeIfPresent(9, (num, val) -> null);
		boolean result=map.containsKey(9); // false
		log.info("9={}",result); // val33

		// computeIfAbsent(), 当 key 不存在时，才会做相关处理
		// 如下：先判断 key 为 23 的元素是否存在，不存在，则添加
		map.computeIfAbsent(23, num -> 230);
		map.containsKey(23); // true

		map.forEach((key, value) -> System.out.println(value));

	}

}
