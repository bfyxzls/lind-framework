package com.lind.common.collection;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

/**
 * @author lind
 * @date 2023/6/8 13:07
 * @since 1.0.0
 */
public class GuavaTest {

	@Test
	public void init() {
		List<Integer> numbers = Lists.newArrayList(1, 2, 3, 4, 5);

		Iterable<Integer> filteredNumbers = Iterables.filter(numbers, n -> n % 2 == 0);
		System.out.println("Filtered numbers: " + filteredNumbers); // 输出：Filtered
																	// numbers: [2, 4]
		Iterable<String> transformedNumbers = Iterables.transform(numbers, n -> "Number: " + n);
		System.out.println("Transformed numbers: " + transformedNumbers);
		// 输出：Transformed numbers: [Number: 1, Number: 2, Number: 3, Number: 4, Number: 5]
	}

	@Test
	public void init2() {
		// 使用静态工厂方法
		ImmutableMap<String, Integer> map1 = ImmutableMap.of("key1", 1, "key2", 2, "key3", 3);

		// 使用构建器
		ImmutableMap<String, Integer> map2 = ImmutableMap.<String, Integer>builder().put("key1", 1).put("key2", 2)
				.put("key3", 3).build();
	}

	@Test
	public void sqrt() {
		int num = 23;
		for (int i = 2; i <= Math.sqrt(num); i++) {
			if (num % i == 0) {
				System.out.println("不是质数：" + i);
			}
		}
	}

	@Test
	public void cache() {
		Cache<String, Integer> cache = CacheBuilder.newBuilder().maximumSize(100).build();

		cache.put("key1", 10);
		cache.put("key2", 20);

		int value1 = cache.getIfPresent("key1");
		System.out.println("Value 1: " + value1); // 输出：Value 1: 10

		int value2 = cache.getIfPresent("key2");
		System.out.println("Value 2: " + value2); // 输出：Value 2: 20
	}

	@Test
	public void str() {
		String str = "Hello,World,Guava";

		boolean isNullOrEmpty = Strings.isNullOrEmpty(str);
		System.out.println("Is string null or empty? " + isNullOrEmpty); // 输出：Is string
																			// null or
																			// empty?
																			// false

		Iterable<String> splitStrings = Splitter.on(',').split(str);
		System.out.println("Split strings: " + splitStrings); // 输出：Split strings: [Hello,
																// World, Guava]

		String joinedString = Joiner.on('-').join(splitStrings);
		System.out.println("Joined string: " + joinedString); // 输出：Joined string:
																// Hello-World-Guava
	}

}
