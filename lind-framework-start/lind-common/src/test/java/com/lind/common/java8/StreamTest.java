package com.lind.common.java8;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author lind
 * @date 2023/3/27 9:39
 * @since 1.0.0
 */
@Slf4j
public class StreamTest {

	static List<Person> personInfos = Arrays.asList(
			new PersonInfo("张三", 1000, "北京", "朝阳区", new String[] { "篮球", "足球" }),
			new PersonInfo("李四", 2000, "上海", "浦东区", new String[] { "篮球", "排球" }),
			new PersonInfo("王五", 3000, "广州", "天河区", new String[] { "羽毛球", "足球" }),
			new PersonInfo("赵六", 4000, "深圳", "南山区", new String[] { "篮球", "乒乓球" }));

	/**
	 * 1.将Person转换成PersonInfo类型的集合 2.过滤出薪水大于2000的人
	 */
	@Test
	public void mapReduce() {
		List<Integer> array = Arrays.asList(3, 4, 2, 1, 5);
		int result = array.parallelStream().filter(o -> o % 2 == 0).sorted().map(x -> x * 2).reduce(1, (e, c) -> e + c);
		log.info("result:{}", result);// 13
	}

	/**
	 * 1.将Person转换成PersonInfo类型的集合 2.过滤出薪水大于2000的人
	 */
	@Test
	public void objectMapCast() {
		personInfos.stream().map(PersonInfo.class::cast).filter(o -> o.getSalary() > 2000).forEach(System.out::println);
	}

	/**
	 * 1.将Person转换成PersonInfo类型的集合 2.过滤出爱好是足球的人
	 */
	@Test
	public void objectMap() {

		List<PersonInfo> personInfoList = personInfos.stream().map(PersonInfo.class::cast)
				.map(mapper -> Arrays.stream(mapper.getHobbies()).filter(type -> Objects.equals("足球", type))
						.map(type -> mapper).findFirst().orElse(null))
				.filter(Objects::nonNull).collect(Collectors.toList());
		personInfoList.forEach(System.out::println);

	}

	// Parallel Sort
	@Test
	public void test24() {
		int max = 1000000;
		List<String> values = new ArrayList<>(max);
		for (int i = 0; i < max; i++) {
			UUID uuid = UUID.randomUUID();
			values.add(uuid.toString());
		}
		long t0 = System.nanoTime();
		long count = values.parallelStream().sorted().count();
		System.out.println(count);
		long t1 = System.nanoTime();
		long millis = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
		System.out.println(String.format("parallel sort took: %d ms", millis));
		// parallel sort took: 385 ms
	}

	static abstract class Person {

	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	static class PersonInfo extends Person {

		private String name;

		private double salary; // 薪水

		private String city; // 城市

		private String address; // 地址

		private String[] hobbies; // 爱好

	}

}
